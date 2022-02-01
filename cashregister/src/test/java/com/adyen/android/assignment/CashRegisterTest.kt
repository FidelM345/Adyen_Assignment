package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import com.adyen.android.assignment.money.MonetaryElement
import com.google.common.truth.Truth
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.hamcrest.core.StringContains
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CashRegisterTest {

    @Test
    fun `performTransaction returns Change object if customer change is successfully given change from the cash register`() {
        val moneyInCashRegister = Change().add(Bill.TWO_HUNDRED_EURO, 3).add(Bill.TEN_EURO, 10)

        val amountPaidByCustomer = Change().add(Bill.TWO_HUNDRED_EURO, 1)

        val change =
            CashRegister(moneyInCashRegister).performTransaction(100_00, amountPaidByCustomer)

        Truth.assertThat(moneyInCashRegister).isEqualTo(change)


    }

    @Test
    fun `should return minimalAmount OF Bills Or Coins To Give The Customer as change`() {
        val moneyInCashRegister = Change().add(Bill.TWO_HUNDRED_EURO, 3).add(Bill.TEN_EURO, 10)

        val amountPaidByCustomer = Change().add(Bill.TWO_HUNDRED_EURO, 1)

        val coinsOrBillsMap =
            CashRegister(moneyInCashRegister).run {
                performTransaction(100_00, amountPaidByCustomer)
                minimalAmountOFBillsOrCoinsToGiveTheCustomer()
            }
        val expected = HashMap<MonetaryElement, Int>().apply {
            put(Bill.TEN_EURO, 10)
        }

        // assertEquals(expected[Bill.TEN_EURO], coinsOrBillsMap[Bill.TEN_EURO])
        Truth.assertThat(expected[Bill.TEN_EURO]).isEqualTo(coinsOrBillsMap[Bill.TEN_EURO])

    }


    @Test
    fun `performTransaction FAILS if amount paid by customer is less than price of product`() {
        val moneyInCashRegister = Change().add(Bill.TWO_HUNDRED_EURO, 1)

        val amountPaidByCustomer = Change().add(Bill.ONE_HUNDRED_EURO, 1)

        try {
            val change =
                CashRegister(moneyInCashRegister).performTransaction(200_00, amountPaidByCustomer)

        } catch (e: CashRegister.TransactionException) {

            Truth.assertThat(e.message)
                .isEqualTo("The price of product is greater than amount paid by customer")
        }
    }

    @Test
    fun `performTransaction FAILS if customer change is greater than total amount of money in cash register`() {
        val moneyInCashRegister = Change().add(Bill.TWO_HUNDRED_EURO, 1)

        val amountPaidByCustomer = Change().add(Bill.FIVE_HUNDRED_EURO, 1)

        try {
            val change =
                CashRegister(moneyInCashRegister).performTransaction(200_00, amountPaidByCustomer)

        } catch (e: CashRegister.TransactionException) {
            /*    assertThat(
                    e.message,
                    StringContains("You have insufficient money in the register to pay customer change")
                )
    */
            Truth.assertThat(e.message)
                .isEqualTo("You have insufficient money in the register to pay customer change")

        }
    }

    @Test
    fun `performTransaction FAILS if there is no right denomination to pay change`() {
        val moneyInCashRegister = Change().add(Bill.TWO_HUNDRED_EURO, 3)

        val amountPaidByCustomer = Change().add(Bill.FIVE_HUNDRED_EURO, 1)

        try {
            val change =
                CashRegister(moneyInCashRegister).performTransaction(200_00, amountPaidByCustomer)

        } catch (e: CashRegister.TransactionException) {

            Truth.assertThat(e.message)
                .isEqualTo("Do not have the right currency denomination to give change")

        }
    }
}
