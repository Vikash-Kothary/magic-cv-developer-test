package fit.magic.cv.repcounter.user

import android.util.Log
import com.google.mediapipe.tasks.components.containers.Landmark
import fit.magic.cv.PoseLandmarkerHelper
import kotlin.math.acos
import kotlin.math.sqrt

// Models

enum class UserPresence {
    NOT_VISIBLE,
    VISIBLE
}

// Interfaces

interface UserService {
    // Update user landmarks
    fun updateUser(resultBundle: PoseLandmarkerHelper.ResultBundle)
    // Derive user details
    fun getPresence(): UserPresence
    fun getUser(): User
}

// Implementations

class UserServiceImpl: UserService {
    companion object {
        private const val TAG = "UserServiceImpl"
        const val WAITING_FOR_USER_THRESHOLD_FRAMES: Int = 5
    }

    private var presence: UserPresence = UserPresence.NOT_VISIBLE
    private var user: User? = null
    private var waitingForUserRemainingFrames = WAITING_FOR_USER_THRESHOLD_FRAMES

    override fun getPresence(): UserPresence = presence

    override fun getUser(): User = user!!

    private fun updateUserPresence(userIsInFrame: Boolean) {
        Log.d(TAG, "updateUserPresence: userIsInFrame: $userIsInFrame")
        Log.d(TAG, "updateUserPresence: waitingForUserRemainingFrames: $waitingForUserRemainingFrames")
        // If the user disappears, start countdown
        if (!userIsInFrame && waitingForUserRemainingFrames > 0) {
            waitingForUserRemainingFrames -= 1
        }
        // If the user doesn't return before the threshold, update state
        if (!userIsInFrame && waitingForUserRemainingFrames <= 0) {
            presence = UserPresence.NOT_VISIBLE
        }
        // If the user returns, reset countdown
        if (userIsInFrame && waitingForUserRemainingFrames != WAITING_FOR_USER_THRESHOLD_FRAMES) {
            waitingForUserRemainingFrames = WAITING_FOR_USER_THRESHOLD_FRAMES
            presence = UserPresence.VISIBLE
        }
    }

    override fun updateUser(resultBundle: PoseLandmarkerHelper.ResultBundle) {
        Log.d(TAG, "updateUserLandmarks")
        Log.v(TAG, "Results Size: ${resultBundle.results.size}")
        if (resultBundle.results.isEmpty()) return
        Log.v(TAG, "Image Landmarks: ${resultBundle.results[0].landmarks().size}")
        val worldLandmarks = resultBundle.results[0].worldLandmarks()
        Log.v(TAG, "World Landmarks: ${worldLandmarks.size}")

        // frames without the user are only used to update the presence
        val userIsInFrame = worldLandmarks.isNotEmpty()
        updateUserPresence(userIsInFrame)
        if(!userIsInFrame) return

        // Use user model in exercise service
        val userInFrame = getUserFromLandmarks(worldLandmarks)
        user = userInFrame
        Log.d(TAG, "User: ${user.toString()}")

    }
}

private fun getUserFromLandmarks(worldLandmarks: List<MutableList<Landmark>>): User {
    val leftHip = worldLandmarks[0][23]
    val rightHip = worldLandmarks[0][24]
    val leftKnee = worldLandmarks[0][25]
    val rightKnee = worldLandmarks[0][26]
    val leftAnkle = worldLandmarks[0][27]
    val rightAnkle = worldLandmarks[0][28]
    return User(
        nose = worldLandmarks[0][0],
        leftEyeInner = worldLandmarks[0][1],
        leftEye = worldLandmarks[0][2],
        leftEyeOuter = worldLandmarks[0][3],
        rightEyeInner = worldLandmarks[0][4],
        rightEye = worldLandmarks[0][5],
        rightEyeOuter = worldLandmarks[0][6],
        leftEar = worldLandmarks[0][7],
        rightEar = worldLandmarks[0][8],
        mouthLeft = worldLandmarks[0][9],
        mouthRight = worldLandmarks[0][10],
        leftShoulder = worldLandmarks[0][11],
        rightShoulder = worldLandmarks[0][12],
        leftElbow = worldLandmarks[0][13],
        rightElbow = worldLandmarks[0][14],
        leftWrist = worldLandmarks[0][15],
        rightWrist = worldLandmarks[0][16],
        leftPinky = worldLandmarks[0][17],
        rightPinky = worldLandmarks[0][18],
        leftIndex = worldLandmarks[0][19],
        rightIndex = worldLandmarks[0][20],
        leftThumb = worldLandmarks[0][21],
        rightThumb = worldLandmarks[0][22],
        leftHip = leftHip,
        rightHip = rightHip,
        leftKnee = leftKnee,
        rightKnee = rightKnee,
        leftAnkle = leftAnkle,
        rightAnkle = rightAnkle,
        leftHeel = worldLandmarks[0][29],
        rightHeel = worldLandmarks[0][30],
        leftFootIndex = worldLandmarks[0][31],
        rightFootIndex = worldLandmarks[0][32],
        //
        leftKneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle),
        rightKneeAngle = calculateAngle(rightHip, rightKnee, rightAnkle),
        //
        strideLength = calculateDistance(leftAnkle, rightAnkle)
    )
}

fun calculateAngle(pointA: Landmark, pointB: Landmark, pointC: Landmark): Float {
    val baX = pointA.x() - pointB.x()
    val baY = pointA.y() - pointB.y()
    val baZ = pointA.z() - pointB.z()
    val bcX = pointC.x() - pointB.x()
    val bcY = pointC.y() - pointB.y()
    val bcZ = pointC.z() - pointB.z()

    val dotProduct = baX * bcX + baY * bcY + baZ * bcZ
    val magnitudeAB = sqrt((baX * baX + baY * baY + baZ * baZ).toDouble())
    val magnitudeBC = sqrt((bcX * bcX + bcY * bcY + bcZ * bcZ).toDouble())

    val angleInRadians = acos(dotProduct / (magnitudeAB * magnitudeBC))
    val angleInDegrees = Math.toDegrees(angleInRadians).toFloat()
    return angleInDegrees
}

fun calculateDistance(pointA: Landmark, pointB: Landmark): Float {
    val dx = pointB.x() - pointA.x()
    val dy = pointB.y() - pointA.y()
    val dz = pointB.z() - pointA.z()
    return sqrt((dx * dx + dy * dy + dz * dz).toDouble()).toFloat()
}