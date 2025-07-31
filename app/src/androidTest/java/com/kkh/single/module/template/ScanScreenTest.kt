import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.datastore.dataStoreFile
import com.kkh.single.module.template.MainActivity
import com.kkh.single.module.template.R
import com.kkh.single.module.template.presentation.scan.CustomIconBox
import com.kkh.single.module.template.presentation.scan.ScanScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert

@RunWith(AndroidJUnit4::class)
class ScanScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        Intents.init()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = context.dataStoreFile("app_preferences.preferences_pb")
        if (file.exists()) {
            file.delete()
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    private fun waitForInit(){
        composeTestRule.onNodeWithText("중환자실").performClick()
        composeTestRule.waitForIdle()
    }

    /** 부서 선택 다이얼로그 노출 및 작동, 부서명 노출 검증 **/
    @Test
    fun `초기화시_부서명_노출`() {
        // 최초 진입 시 다이얼로그 등장: 부서 이름 셋 중 하나가 보이면 등장한 것
        waitForInit()
        // 다이얼로그 닫히고 부서명이 그레이컬러로 화면에 노출되는지 확인
        composeTestRule.onNodeWithText("중환자실").assertExists()
    }

    /** 타이틀, 서브텍스트, 주요 버튼 노출 확인 **/
    @Test
    fun `ScanScreen_타이틀_서브텍스트_주요버튼_노출`() {
        // 타이틀("QR 코드 ... 스캔해주세요.") highlight 텍스트 및 약포지 텍스트 등 존재 여부
        // UI 상태 대기 (비동기 처리 보장)
        waitForInit()
        // "QR 코드" 등 텍스트의 실존을 검증
        composeTestRule.onNodeWithTag("text_barcode_scan").assertExists()
    }

    /** CustomIconBox 클릭 5회 시 인텐트 발송(로그 전송) 검증 **/
    @Test
    fun `버튼_5번_클릭_시_이메일_전송`() {
        waitForInit()
        val iconNode = composeTestRule.onNodeWithTag("box_email")
        repeat(5) {
            iconNode.performClick()
        }
        Intents.intended(IntentMatchers.hasAction("android.intent.action.SEND"))
        Intents.intended(IntentMatchers.hasType("text/plain"))
        Intents.intended(IntentMatchers.hasExtra("android.intent.extra.SUBJECT", "HTTP Log File"))
    }
}
