package app.dev.atmsimulator.homeScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(): ViewModel(){

    private var _uiState = MutableStateFlow(BalanceUiState())
    var uiState = _uiState.asStateFlow()

    private var _withdrawalUiState = MutableStateFlow(WithdrawalUiState())
    var withdrawalUiState = _withdrawalUiState.asStateFlow()

    fun withdraw(amount: Int) {

        val balance = _uiState.value

        if (amount <= 0 || amount % 100 != 0) {
            _withdrawalUiState.value = WithdrawalUiState(message = "Invalid amount")
            return
        }

        if (amount > balance.totalBalance) {
            _withdrawalUiState.value = WithdrawalUiState(message = "Insufficient balance")
            return
        }

        var remaining = amount

        val n2000 = minOf(remaining / 2000, balance.n2000)
        remaining = remaining - n2000 * 2000

        val n500 = minOf(remaining / 500, balance.n500)
        remaining = remaining - n500 * 500

        val n200 = minOf(remaining / 200, balance.n200)
        remaining = remaining - n200 * 200

        val n100 = minOf(remaining / 100, balance.n100)
        remaining = remaining - n100 * 100

        if (remaining != 0) return

        _uiState.value = balance.copy(
            totalBalance = balance.totalBalance - amount,
            n2000 = balance.n2000 - n2000,
            n500 = balance.n500 - n500,
            n200 = balance.n200 - n200,
            n100 = balance.n100 - n100
        )

        _withdrawalUiState.value = WithdrawalUiState(
            n2000 = n2000,
            n500 = n500,
            n200 = n200,
            n100 = n100,
            message = "Withdrawal Successful"
        )
    }


}

data class BalanceUiState(
    var totalBalance: Int = 50000,
    var n2000: Int = 10,
    var n500: Int = 40,
    var n200: Int = 25,
    var n100: Int = 50,
)

data class WithdrawalUiState(
    var n2000: Int = 0,
    var n500: Int = 0,
    var n200: Int = 0,
    var n100: Int = 0,
    var message: String = ""
)