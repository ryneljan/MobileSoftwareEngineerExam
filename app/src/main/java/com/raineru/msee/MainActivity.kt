package com.raineru.msee

import android.icu.util.Calendar
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raineru.msee.ui.theme.MobileSoftwareEngineerExamTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
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
    val mockyApiClient = remember {
        MockyApiClient(
            HttpClient(OkHttp) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
                install(ContentNegotiation) {
                    json(
                        Json {
                            encodeDefaults = true
                            isLenient = true
                            allowSpecialFloatingPointValues = true
                            allowStructuredMapKeys = true
                            prettyPrint = false
                            useArrayPolymorphism = false
                            ignoreUnknownKeys = true
                        }
                    )
                }
                install(HttpTimeout) {
                    connectTimeoutMillis = 10_000L
                }
            }
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            FullNameTextField()
            EmailTextField()
            PhilippineMobileNumberTextField()

            var selectedDate by remember { mutableStateOf<Long?>(null) }

            DatePickerFieldToModal(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )
            AgeField(
                selectedDate,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            GenderDropdown()

            val coroutineScope = rememberCoroutineScope()

            var result by remember {
                mutableStateOf("")
            }
            Button(
                onClick = {
                    coroutineScope.launch {
//                        val response = mockyApiClient.submitForm(mockIds.random())
                        val response = mockyApiClient.submitForm("unknown_id")
                        result = when (response) {
                            is ApiResponse.Success -> response.data.message
                            is ApiResponse.Error -> response.errorMessage
                        }
                    }
                }
            ) {
                Text("SUBMIT")
            }
            Text(result)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown() {
    var expanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    val genders = listOf("Male", "Female", "Other")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedGender ?: "",
            onValueChange = { },
            readOnly = true,
            label = { Text("Gender") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(text = gender) },
                    onClick = {
                        selectedGender = gender
                        expanded = false
                    }
                )
            }
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
fun DatePickerFieldToModal(
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
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
            onDateSelected = { onDateSelected(it) },
            onDismiss = { showModal = false }
        )
    }


}

@Composable
fun AgeField(
    selectedDate: Long?,
    modifier: Modifier = Modifier
) {
    val ageText = when (val age = calculateAge(selectedDate)) {
        1 -> {
            "Age: $age year"
        }

        null -> {
            "Age: "
        }

        else -> {
            "Age: $age years"
        }
    }
    Text(
        text = ageText,
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        modifier = modifier
    )
}

fun calculateAge(birthDateMillis: Long?): Int? {
    if (birthDateMillis == null) return null

    val birthCalendar = Calendar.getInstance().apply {
        timeInMillis = birthDateMillis
    }
    val today = Calendar.getInstance()

    var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
        age--
    }

    return age
}

val mockIds = listOf(
    "a582905b-cdec-472b-ba2a-6712f7352042",
    "1a774192-ec97-49f4-9d05-68b3315d74e7"
)
