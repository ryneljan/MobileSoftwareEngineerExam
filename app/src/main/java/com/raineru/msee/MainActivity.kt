package com.raineru.msee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raineru.msee.ui.theme.MobileSoftwareEngineerExamTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileSoftwareEngineerExamTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            FullNameTextField()
            EmailTextField()
            PhilippineMobileNumberTextField()
            DatePickerFieldToModal()
        }
    }
}

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = email,
        onValueChange = {
            email = it
            isEmailValid = it.isValidEmail()
        },
        label = { Text("Email") },
        isError = isEmailValid,
        textStyle = TextStyle(fontSize = 16.sp),
        supportingText = {
            if (!isEmailValid) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Invalid Email",
                    color = Color.Red
                )
            }
        }
    )
}

@Composable
fun FullNameTextField(
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var isFullNameValid by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = fullName,
        onValueChange = {
            fullName = it
            isFullNameValid = it.isValidFullName()
        },
        label = { Text("Full Name") },
        isError = isFullNameValid,
        textStyle = TextStyle(fontSize = 16.sp),
        supportingText = {
            if (!isFullNameValid) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Invalid Full Name",
                    color = Color.Red
                )
            }
        }
    )
}

@Composable
fun PhilippineMobileNumberTextField(
    modifier: Modifier = Modifier
) {
    var mobileNumber by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = mobileNumber,
        onValueChange = {
            mobileNumber = it
            isError = !it.isValidPhilippineMobileNumber()
        },
        label = { Text("Mobile Number") },
        isError = isError,
        textStyle = TextStyle(fontSize = 16.sp),
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Invalid Mobile Number",
                    color = Color.Red
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MobileSoftwareEngineerExamTheme {
        HomeScreen()
    }
}

fun String.isValidFullName(): Boolean {
    val trimmedString = this.trim()
    val regex = Regex("^[a-zA-Z]+( [a-zA-Z]+|[.,])*$")
    return regex.matches(trimmedString)
}

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    return emailRegex.matches(this)
}

fun String.isValidPhilippineMobileNumber(): Boolean {
    val regex = Regex("^(09)\\d{9}\$")
    return regex.matches(this)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("Date of Birth") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}