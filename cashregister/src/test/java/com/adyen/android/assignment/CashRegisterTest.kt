package com.adyen.android.assignment

import com.adyen.android.assignment.CashRegister.TransactionException
import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CashRegisterTest {

    @Test
    fun `when change is available, the minimal amount of change is returned`() {
        val initChange = Change()
            .add(Bill.FIFTY_EURO, 1)
            .add(Bill.TWENTY_EURO, 3)
            .add(Coin.TWO_EURO, 5)
            .add(Coin.TWENTY_CENT, 2)
            .add(Coin.TEN_CENT, 4)
            .add(Coin.FIVE_CENT, 1)
        val cashRegister = CashRegister(initChange)

        val price = 139_55L
        val paid = Change().add(Bill.ONE_HUNDRED_EURO, 2)
        val expected = Change()
            .add(Bill.TWENTY_EURO, 3)
            .add(Coin.TWENTY_CENT, 2)
            .add(Coin.FIVE_CENT, 1)

        assertEquals(expected, cashRegister.performTransaction(price, paid))
    }

    @Test
    fun `when no change is available, a TransactionException is thrown`() {
        val cashRegister = CashRegister(Change.none())

        val price = 520_45L
        val paid = Change()
            .add(Bill.FIVE_HUNDRED_EURO, 1)
            .add(Bill.ONE_HUNDRED_EURO, 1)

        assertThrows(TransactionException::class.java) {
            cashRegister.performTransaction(price, paid)
        }
    }

    @Test
    fun `when the amount paid equals the price, Change none() is returned`() {
        val cashRegister = CashRegister(Change.none())

        val price = 102_05L
        val paid = Change()
            .add(Bill.ONE_HUNDRED_EURO, 1)
            .add(Coin.ONE_EURO, 2)
            .add(Coin.FIVE_CENT, 1)
        val expected = Change.none()

        assertEquals(expected, cashRegister.performTransaction(price, paid))
    }


    @Test
    fun `when the amount paid is less than the price, a TransactionException is thrown`() {
        val cashRegister = CashRegister(Change.none())

        val price = 151_05L
        val paid = Change()
            .add(Bill.ONE_HUNDRED_EURO, 1)
            .add(Bill.FIFTY_EURO, 1)
            .add(Coin.ONE_EURO, 1)
            .add(Coin.TWO_CENT, 2)

        assertThrows(TransactionException::class.java) {
            cashRegister.performTransaction(price, paid)
        }
    }

    @Test
    fun `when a transaction is completed, register state is updated for subsequent transactions`() {
        val initChange = Change().add(Bill.ONE_HUNDRED_EURO, 1)
        val cashRegister = CashRegister(initChange)

        val firstPrice = 300_00L
        val firstTransaction = Change().add(Bill.TWO_HUNDRED_EURO, 2)
        val firstExpectedChange = Change().add(Bill.ONE_HUNDRED_EURO, 1)
        assertEquals(
            firstExpectedChange,
            cashRegister.performTransaction(firstPrice, firstTransaction)
        )

        val secondPrice = 100_00L
        val secondTransaction = Change().add(Bill.FIVE_HUNDRED_EURO, 1)
        val secondExpectedChange = Change().add(Bill.TWO_HUNDRED_EURO, 2)
        assertEquals(
            secondExpectedChange,
            cashRegister.performTransaction(secondPrice, secondTransaction)
        )
    }
}
