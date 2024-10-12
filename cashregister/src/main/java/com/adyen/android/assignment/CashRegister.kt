package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import com.adyen.android.assignment.money.MonetaryElement

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private val change: Change) {
    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(price: Long, amountPaid: Change): Change {
        if (amountPaid.total < price) {
            throw TransactionException("The amount paid  [${amountPaid.total}]  must be equal to or greater than the price [$price]")
        }
        val changeExpectedTotal = amountPaid.total - price
        change.add(amountPaid)

        val changeToGive = calculateChange(changeExpectedTotal)
        if (changeToGive == null) {
            change.remove(amountPaid)
            throw TransactionException("Insufficient funds")
        } else {
            change.remove(changeToGive)
            return changeToGive
        }
    }

    private fun calculateChange(changeToGive: Long): Change? {
        val denominations = (Bill.entries + Coin.entries)
            .filter { change.getCount(it) > 0 }
        val expectedChange = changeToGive.toInt()
        val minDenomsAmount = Array(expectedChange + 1) { Int.MAX_VALUE }
        val usedDenoms = Array<MutableMap<MonetaryElement, Int>?>(expectedChange + 1) { null }

        minDenomsAmount[0] = 0
        usedDenoms[0] = mutableMapOf()

        for (current in 1..expectedChange) {
            for (denom in denominations) {
                val value = denom.minorValue
                if (value <= current && minDenomsAmount[current - value] != Int.MAX_VALUE) {
                    val available = change.getCount(denom)
                    val usedAmount = usedDenoms[current - value]?.get(denom) ?: 0
                    if (usedAmount < available) {
                        val allDenomsAmount = minDenomsAmount[current - value] + 1
                        if (allDenomsAmount < minDenomsAmount[current]) {
                            minDenomsAmount[current] = allDenomsAmount
                            usedDenoms[current] = usedDenoms[current - value]?.toMutableMap() ?: mutableMapOf()
                            usedDenoms[current]?.set(denom, usedAmount + 1)
                        }
                    }
                }
            }
        }

        usedDenoms[expectedChange]?.let { resultDenoms ->
            return Change().apply { resultDenoms.forEach(::add) }
        }
        return null
    }

    class TransactionException(message: String, cause: Throwable? = null) :
        Exception(message, cause)
}
