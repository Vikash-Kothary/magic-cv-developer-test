package fit.magic.cv.repcounter.exercise

import fit.magic.cv.repcounter.user.User
import org.junit.Assert.assertEquals
import org.junit.Test

class AlternatingLungeExerciseServiceTest {

    private val exerciseService = AlternatingLungeExerciseService()

    @Test
    fun getUserFeedback_shouldBeLeft() {
        val expected = "LEFT"

        val user = mockUser()
        exerciseService.updateUserState(user)
        val actual = exerciseService.getUserFeedback()

        assertEquals(expected, actual)
    }


    @Test
    fun getUserFeedback_shouldBeRight() {
        val expected = "RiGHT"

        val user = mockUser()
        exerciseService.updateUserState(user)
        val actual = exerciseService.getUserFeedback()

        assertEquals(expected, actual)
    }

}

// TODO: Need to use 3d float tuple instead of Landmark for easier mocking
private fun mockUser(): User = User(
    nose = TODO(),
    leftEyeInner = TODO(),
    leftEye = TODO(),
    leftEyeOuter = TODO(),
    rightEyeInner = TODO(),
    rightEye = TODO(),
    rightEyeOuter = TODO(),
    leftEar = TODO(),
    rightEar = TODO(),
    mouthLeft = TODO(),
    mouthRight = TODO(),
    leftShoulder = TODO(),
    rightShoulder = TODO(),
    leftElbow = TODO(),
    rightElbow = TODO(),
    leftWrist = TODO(),
    rightWrist = TODO(),
    leftPinky = TODO(),
    rightPinky = TODO(),
    leftIndex = TODO(),
    rightIndex = TODO(),
    leftThumb = TODO(),
    rightThumb = TODO(),
    leftHip = TODO(),
    rightHip = TODO(),
    leftKnee = TODO(),
    rightKnee = TODO(),
    leftAnkle = TODO(),
    rightAnkle = TODO(),
    leftHeel = TODO(),
    rightHeel = TODO(),
    leftFootIndex = TODO(),
    rightFootIndex = TODO(),
    leftKneeAngle = TODO(),
    rightKneeAngle = TODO(),
    strideLength = TODO()
)