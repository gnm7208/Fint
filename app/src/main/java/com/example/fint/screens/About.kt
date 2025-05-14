package com.example.fint.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fint.model.ContactViewModel
import com.example.fint.R
import com.example.fint.components.EmbeddedGoogleMap
import com.example.fint.components.Header
import com.example.fint.ui.theme.FintTheme

@Composable
fun About(
    navController: NavController,
    viewModel: ContactViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState()
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val statusMessage by viewModel.message.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Header()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ...all other content like images, form fields, etc.

            // Images and Content
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.LightGray)
                    .height(250.dp)
                    .width(300.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.faul),
                    contentDescription = stringResource(R.string.header_title),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.LightGray)
                    .height(250.dp)
                    .width(300.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.map),
                    contentDescription = stringResource(R.string.location)
                )
//            EmbeddedGoogleMap()
            }

            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .shadow(8.dp, RoundedCornerShape(25.dp))
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White)
                    .height(250.dp)
                    .width(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.where),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.courses),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = stringResource(R.string.have_a_question))
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = stringResource(R.string.contact_us), fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name_hint)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_hint)) },
                singleLine = true,

                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(stringResource(R.string.message_hint)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    viewModel.saveContact(name, email, message)
                    name = ""
                    email = ""
                    message = ""
                },
                enabled = !isLoading && name.isNotBlank() && email.isNotBlank() && message.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Send Message")
                }
            }

            statusMessage?.let { msg ->
                Text(
                    text = msg,
                    color = if (msg.contains("success", true)) Color.Green else Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AboutPreview(){
    val navController = rememberNavController()
    FintTheme {
        About(navController = navController, scrollState = rememberScrollState())
    }

}