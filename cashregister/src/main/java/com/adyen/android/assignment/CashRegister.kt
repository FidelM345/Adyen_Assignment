package com.adyen.android.assignment

import com.adyen.android.assignment.money.Change
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

    private val changeToGiveTheCustomer = HashMap<MonetaryElement, Int>()


    fun performTransaction(price: Long, amountPaid: Change): Change {
        // TODO: Implement logic.

        var customerChange = amountPaid.total - price

        if (customerChange > change.total) {
            throw TransactionException("You have insufficient money in the register to pay customer change")
        } else if (customerChange < 0) {

            throw TransactionException("The price of product is greater than amount paid by customer")

        } else {

            var moneyElements = change.getElements()
            // val changeToGive = HashMap<MonetaryElement, Int>()

            for (moneyElement in moneyElements) {

                var numberOFMoneyElement = change.getCount(moneyElement)
                var counter = 0

                while (customerChange - moneyElement.minorValue >= 0 && numberOFMoneyElement > 0) {
                    customerChange -= moneyElement.minorValue
                    counter++
                    numberOFMoneyElement--

                }

                if (counter != 0) changeToGiveTheCustomer[moneyElement] = counter
            }

            //subtract change given to the customer from the cash drawer
            for ((key, value) in changeToGiveTheCustomer) {

                change.remove(key, value)
            }

            //if you still are remaining with change to give the customer
            if (customerChange > 0) {
                throw TransactionException("Do not have the right currency denomination to give change")
            }

        }

        return change
    }

    fun minimalAmountOFBillsOrCoinsToGiveTheCustomer(): HashMap<MonetaryElement, Int> {

        return changeToGiveTheCustomer
    }

    class TransactionException(message: String, cause: Throwable? = null) :
        Exception(message, cause)
}
