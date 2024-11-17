// Copyright (c) 2024 Magic Tech Ltd

package fit.magic.cv.repcounter.workout

import fit.magic.cv.PoseLandmarkerHelper
import fit.magic.cv.repcounter.exercise.ExerciseService
import fit.magic.cv.repcounter.exercise.AlternatingLungeExerciseService
import fit.magic.cv.repcounter.user.UserService
import fit.magic.cv.repcounter.user.UserServiceImpl
import fit.magic.cv.repcounter.user.UserPresence


// Models

enum class WorkoutState {
    WAITING_FOR_USER,
    READY,
    DOING,
    DONE,
}

// Interfaces

interface WorkoutService {
    fun setUserLandmarks(resultBundle: PoseLandmarkerHelper.ResultBundle)
    //
    fun setEventListener(listener: WorkoutEventListener?)
}

interface WorkoutEventListener {
    fun onRepProgressUpdated(progress: Float)
    fun onRepCountUpdated(count: Int)
    fun onRepFeedbackUpdated(feedback: String)
}

// Implementations

class WorkoutServiceImpl : WorkoutService {
    companion object {
        const val TAG = "WorkoutServiceImpl"
        const val FRAME_RATE_LIMIT_EVERY = 1
    }

    private val userService: UserService = UserServiceImpl()
    private val exerciseService: ExerciseService = AlternatingLungeExerciseService()
    private var workoutEventListener: WorkoutEventListener? = null

    // Workout Model
    private var state: WorkoutState = WorkoutState.WAITING_FOR_USER
    private val goalReps = 15
    private var currentRep: Int = 0
    private var frameCount = 0

    override fun setEventListener(listener: WorkoutEventListener?) {
        this.workoutEventListener = listener
    }

    /*
    Updates the progress bar. Should be a value between 0 and 1.
     */
    fun setRepProgressPercent(progress: Float) {
        workoutEventListener?.onRepProgressUpdated(progress)
    }

    /*
    Increments the rep count by 1.
     */
    fun incrementRepCount() {
        currentRep++
        workoutEventListener?.onRepCountUpdated(currentRep)
    }

    /*
    Resets the rep count to 0.
    */
    fun resetRepCount() {
        currentRep = 0
        workoutEventListener?.onRepCountUpdated(currentRep)
    }

    private fun getUserFeedback(): String = when(state) {
        WorkoutState.WAITING_FOR_USER -> "Your whole body is ${userService.getPresence().toString().lowercase().replace('_', ' ')} in the mirror."
        WorkoutState.READY -> "Let's get ready to do ${exerciseService.getName()}."
        WorkoutState.DOING -> exerciseService.getUserFeedback()
        WorkoutState.DONE -> "Great job."
        null -> ""
    }

    /*
    Displays a feedback message.
     */
    fun setWorkoutState(state: WorkoutState) {
        if (this.state != state) {
            this.state = state
            workoutEventListener?.onRepFeedbackUpdated(getUserFeedback())
        }
    }

    override fun setUserLandmarks(resultBundle: PoseLandmarkerHelper.ResultBundle) {
        frameCount++
        // TODO: Frame rate averaging

        if (state == WorkoutState.DONE) return

        userService.updateUser(resultBundle)
        exerciseService.setRepNumber(currentRep)

        if (userService.getPresence() == UserPresence.NOT_VISIBLE) {
            this.setWorkoutState(WorkoutState.WAITING_FOR_USER)
        }

        if (frameCount % FRAME_RATE_LIMIT_EVERY != 0) return

        // Assume pose is present in image
        // Assume pose confidence is high enough
        // Assume that only 1 post is present in image
        // Assume that pose detection confidence is high enough
        // Assume user is looking at the camera

        if (state != WorkoutState.DOING && userService.getPresence() == UserPresence.VISIBLE) {
            this.setWorkoutState(WorkoutState.READY)
        }

        if (state == WorkoutState.READY || state == WorkoutState.DOING) {
            exerciseService.updateUserState(userService.getUser())
            this.setRepProgressPercent(exerciseService.getRepProgressPercent())
            this.workoutEventListener?.onRepFeedbackUpdated(exerciseService.getUserFeedback())

        }

        if (state == WorkoutState.READY && exerciseService.getRepProgressPercent() > 0f) {
            this.setWorkoutState(WorkoutState.DOING)
        }

        if (state == WorkoutState.DOING && exerciseService.getRepProgressPercent() >= 1f) {
            this.incrementRepCount()
            exerciseService.resetRepProgressPercent()
        }

        if (currentRep >= goalReps) {
            this.setWorkoutState(WorkoutState.DONE)
        }
    }
}



