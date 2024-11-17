// Copyright (c) 2024 Magic Tech Ltd

package fit.magic.cv.repcounter.exercise

import android.util.Log
import fit.magic.cv.repcounter.user.User

enum class RepSide {
    LEFT,
    RIGHT
}

enum class RepDirection {
    UP,
    DOWN
}

interface ExerciseService {
    fun setRepNumber(repNumber: Int)
    fun updateUserState(user: User)

    fun getName(): String
    fun getUserFeedback(): String
    fun getRepProgressPercent(): Float

    fun resetRepProgressPercent()
}

class AlternatingLungeExerciseService: ExerciseService {
    companion object {
        const val TAG = "AlternatingLungeExerciseService"
        const val LUNGE_STRAIGHT_KNEE_MAX_ANGLE = 180f
        const val LUNGE_STRAIGHT_KNEE_MIN_ANGLE = 150f
        const val LUNGE_BENT_KNEE_MAX_ANGLE = 120f
        const val LUNGE_BENT_KNEE_MIN_ANGLE = 90f
        const val LUNGE_STRIDE_MIN_THRESHOLD = 0.6096
        const val LUNGE_STRIDE_MAX_THRESHOLD = 0.9144
    }

    private val name: String = "Alternating Lunges"

    private var repNumber: Int = 0
    private var repSide: RepSide = RepSide.RIGHT
    private var repProgress = 0.0f
    private var repFeedbackMessages = ""

    override fun getRepProgressPercent(): Float {
        return repProgress
    }

    override fun setRepNumber(repNumber: Int) {
        this.repNumber = repNumber
        if (repNumber % 2 == 0 ) {
            repSide = RepSide.LEFT
        } else {
            repSide = RepSide.RIGHT
        }
    }

    override fun resetRepProgressPercent() {
        repProgress = 0.0f
    }

    override fun getName(): String = name

    override fun getUserFeedback(): String {
        return repFeedbackMessages
    }

    fun getRepDirection(): RepDirection {
        if (repProgress >= 0.5) return RepDirection.UP
        return RepDirection.DOWN
    }

    fun setRepProgress(progress: Float){
        if (progress < 0) return
        if (progress > 1) return
        if (progress > this.repProgress) {
            if (getRepDirection() == RepDirection.DOWN && progress >= 0.5) {
                this.repProgress = 0.5f
            } else if (getRepDirection() == RepDirection.UP && progress >= 1) {
                this.repProgress = 1f
            } else {
                this.repProgress = progress
            }
        }
    }

    override fun updateUserState(user: User) {
        Log.d(TAG, "updateExercise")

        Log.v(TAG, "Rep Number: ${repNumber}")
        // Progress
        // 0-50% = standing to bent knee
        // 50% - 100% = bent knee to standing
//        Log.v(TAG, "Left Hip: ${user.leftHip}")
//        Log.v(TAG, "Left Knee: ${user.leftKnee}")
//        Log.v(TAG, "Left Ankle: ${user.leftAnkle}")
//        Log.v(TAG, "Left Knee Angle: ${user.leftKneeAngle}")
//        Log.v(TAG, "Right Hip: ${user.rightHip}")
//        Log.v(TAG, "Right Knee: ${user.rightKnee}")
//        Log.v(TAG, "Right Ankle: ${user.rightAnkle}")
//        Log.v(TAG, "Right Knee Angle: ${user.rightKneeAngle}")
        repSide = user.getCurrentRepSide()
        Log.v(TAG, "Current Rep Side: ${repSide}")
//        repFeedbackMessages.add(currentRepSide.toString())

        // Debug
        var bentKneeAngle = user.getBentKneeAngle()
        Log.v(TAG, "Bent Knee Angle: ${bentKneeAngle}")

        // LUNGE_STRAIGHT_KNEE_MIN_ANGLE=150f
        // LUNGE_BENT_KNEE_MIN_ANGLE=120f
        val angle = user.getBentKneeAngle()

        setRepProgress((LUNGE_STRAIGHT_KNEE_MIN_ANGLE - angle) / (LUNGE_STRAIGHT_KNEE_MIN_ANGLE - LUNGE_BENT_KNEE_MAX_ANGLE))
        if (getRepDirection() == RepDirection.DOWN && angle < LUNGE_BENT_KNEE_MAX_ANGLE) {
            Log.v(TAG, "RepDirection: DOWN to UP")
            repFeedbackMessages = "Perfect. Return to the standing position."
            setRepProgress(0.5f)
        }
        if (getRepDirection() == RepDirection.UP && angle > LUNGE_STRAIGHT_KNEE_MIN_ANGLE){
            Log.v(TAG, "RepDirection: UP to DOWN")
            repFeedbackMessages = "Next: ${getNextRepSide().toString().lowercase()} leg lunge."
            setRepProgress(1f)
        }
        if (angle in LUNGE_BENT_KNEE_MAX_ANGLE..LUNGE_STRAIGHT_KNEE_MIN_ANGLE) {
            repFeedbackMessages = ""
        }
        Log.v(TAG, "Rep Direction: ${getRepDirection()}")
        Log.v(TAG, "Rep Progress: ${repProgress}")

        // From the video
        // TODO: Take a step 2-3 ft forward from the starting position
//        Log.v(TAG,"Left Ankle: ${user.leftAnkle}")
//        Log.v(TAG,"Right Ankle: ${user.rightAnkle}")
//        Log.v(TAG,"Stride Length: ${user.strideLength}")
//        if (user.getBentKneeAngle() > LUNGE_BENT_KNEE_MIN_ANGLE && user.strideLength < LUNGE_STRIDE_MIN_THRESHOLD) {
//            repFeedbackMessages.add("Try and place your front foot a little further")
//        }
        // TODO: Dropping upper body directly down below your feet
        // TODO: Get your thigh as close to parallel to the ground as you can
        // TODO: Make sure at no times during the exercise, your knee comes over the toes of your front foot

        // TODO: From my experience
        // Look forward
        // Use hands
        // Center of balance

    }

    private fun getNextRepSide(): RepSide {
        if (repSide == RepSide.LEFT) return RepSide.RIGHT
        return RepSide.LEFT
    }

    private fun User.getCurrentRepSide(): RepSide {
        if (repNumber % 2 == 1) {
            return RepSide.LEFT
        } else {
            return RepSide.RIGHT
        }
    }
}



private fun User.getBentKneeAngle(): Float {
    if (leftKneeAngle > rightKneeAngle) {
        return leftKneeAngle
    } else {
        return rightKneeAngle
    }
}




