package com.example.snaplingo.ui.theme.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.snaplingo.navigation.Screen
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

@Composable
fun CameraScreen(
    paddingValues: PaddingValues,
    navController: NavController
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    var cameraProvider by remember {
        mutableStateOf<ProcessCameraProvider?>(null)
    }

    var recognizedText by remember {
        mutableStateOf("")
    }

    val previewView = remember {
        PreviewView(context)
    }

    // START CAMERA

    LaunchedEffect(Unit) {

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            val provider =
                cameraProviderFuture.get()

            cameraProvider = provider

            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider =
                        previewView.surfaceProvider
                }

            val capture = ImageCapture.Builder()
                .build()

            imageCapture = capture

            val cameraSelector =
                CameraSelector.DEFAULT_BACK_CAMERA

            try {

                provider.unbindAll()

                provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    capture
                )

            } catch (exception: Exception) {

                exception.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(context))
    }

    // OCR

    fun recognizeText(photoFile: File) {

        val image = InputImage.fromFilePath(
            context,
            Uri.fromFile(photoFile)
        )

        val recognizer = TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )

        recognizer.process(image)
            .addOnSuccessListener { visionText ->

                recognizedText = visionText.text
            }
            .addOnFailureListener { exception ->

                exception.printStackTrace()
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        // CAMERA

        if (recognizedText.isEmpty()) {

            AndroidView(
                factory = {
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            Button(
                onClick = {

                    val capture = imageCapture
                        ?: return@Button

                    val photoFile = File(
                        context.cacheDir,
                        "snaplingo_${System.currentTimeMillis()}.jpg"
                    )

                    val outputOptions =
                        ImageCapture.OutputFileOptions
                            .Builder(photoFile)
                            .build()

                    capture.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),

                        object : ImageCapture.OnImageSavedCallback {

                            override fun onImageSaved(
                                outputFileResults:
                                ImageCapture.OutputFileResults
                            ) {

                                // UGASI KAMERU
                                cameraProvider?.unbindAll()

                                // POKRENI OCR
                                recognizeText(photoFile)
                            }

                            override fun onError(
                                exception: ImageCaptureException
                            ) {

                                exception.printStackTrace()
                            }
                        }
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            ) {

                Text("Take Photo")
            }

        } else {

            // OCR RESULT

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top
            ) {

                Text(
                    text = "Recognized Text",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = recognizedText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                Button(
                    onClick = {

                        recognizedText = ""

                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp)
                ) {

                    Text("Retake")
                }
                Button(
                    onClick = {

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(
                                "recognizedText",
                                recognizedText
                            )

                        navController.navigate(
                            Screen.LanguageSelection.route
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp)
                ) {

                    Text("Use this text")
                }
            }
        }
    }
}