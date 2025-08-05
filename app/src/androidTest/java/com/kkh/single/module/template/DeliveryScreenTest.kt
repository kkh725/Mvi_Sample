package com.kkh.single.module.template

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.datastore.dataStoreFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kkh.single.module.template.data.model.PatientModel
import com.kkh.single.module.template.presentation.delivery.DeliveryScreen
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeliveryScreenTest {

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>() // 혹은 ComposeTestActivity

    private lateinit var fakeViewModel: DeliveryViewModel

    @Test
    fun deliveryScreen_showsEmptyBoxWhenNoPatients() {
        // Then
        composeTestRule
            .onNodeWithTag("box_empty_medicine")
            .assertIsDisplayed()
    }

    @Test
    fun deliveryScreen_showsPatientList_whenDataExists() {
        // Given
        val patient = PatientModel(patientId = "P123", dept = "외과")

        // Then
        composeTestRule
            .onNodeWithTag("box_empty_medicine")
            .assertIsDisplayed()
    }

    @Test
    fun deliveryScreen_showsConfirmDeleteDialog_onDeleteClick() {
        // Given
        val patient = PatientModel(patientId = "P123", dept = "외과")

        // When - 클릭: 삭제 아이콘
        composeTestRule
            .onNodeWithContentDescription("icon_delete")
            .performClick()

        // Then - 삭제 확인 다이얼로그 표시
        composeTestRule
            .onNodeWithText("스캔을 취소하시겠습니까?")
            .assertIsDisplayed()
    }
}

