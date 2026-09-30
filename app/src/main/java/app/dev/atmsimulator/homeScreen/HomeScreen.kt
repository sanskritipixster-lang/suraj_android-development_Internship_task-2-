package app.dev.atmsimulator.homeScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    var amount by remember { mutableStateOf("") }
    val balanceUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val withdrawalUiState by viewModel.withdrawalUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(withdrawalUiState.message) {
        if (withdrawalUiState.message.isNotEmpty()) {
            Toast.makeText(context, withdrawalUiState.message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("ATM Simulator", fontSize = 30.sp)

        Text(
            text = "Total Balance: ₹${balanceUiState.totalBalance}",
            fontSize = 22.sp
        )

        Text("Available Notes:", fontSize = 18.sp)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NotesItem(2000, balanceUiState.n2000)
                NotesItem(500, balanceUiState.n500)
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NotesItem(200, balanceUiState.n200)
                NotesItem(100, balanceUiState.n100)
            }
        }

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it.filter(Char::isDigit) },
            label = { Text("Enter amount", color = Color.Black) },
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                viewModel.withdraw(amount.toIntOrNull() ?: 0)
                amount = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Withdraw")
        }

        Text("Withdrawal Result", fontSize = 18.sp)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("₹2000 notes: ${withdrawalUiState.n2000}")
                Text("₹500 notes: ${withdrawalUiState.n500}")
                Text("₹200 notes: ${withdrawalUiState.n200}")
                Text("₹100 notes: ${withdrawalUiState.n100}")
            }
        }
    }
}

@Composable
fun NotesItem(currency: Int, count: Int) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "₹$currency Notes",
            fontSize = 18.sp,
            color = Color.White
        )

        Text(
            text = "$count Notes",
            fontSize = 14.sp,
            color = Color.White
        )
    }
}