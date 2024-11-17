// Copyright (c) 2024 Magic Tech Ltd

package fit.magic.cv.repcounter.user

import com.google.mediapipe.tasks.components.containers.Landmark

data class User(
    // Nodes
    val nose: Landmark, // 0
    val leftEyeInner: Landmark, // 1
    val leftEye: Landmark, // 2
    val leftEyeOuter: Landmark, // 3
    val rightEyeInner: Landmark, // 4
    val rightEye: Landmark, // 5
    val rightEyeOuter: Landmark, // 6
    val leftEar: Landmark, // 7
    val rightEar: Landmark, // 8
    val mouthLeft: Landmark, // 9
    val mouthRight: Landmark, // 10
    val leftShoulder: Landmark, // 11
    val rightShoulder: Landmark, // 12
    val leftElbow: Landmark, // 13
    val rightElbow: Landmark, // 14
    val leftWrist: Landmark, // 15
    val rightWrist: Landmark, // 16
    val leftPinky: Landmark, // 17
    val rightPinky: Landmark, // 18
    val leftIndex: Landmark, // 19
    val rightIndex: Landmark, // 20
    val leftThumb: Landmark, // 21
    val rightThumb: Landmark, // 22
    val leftHip: Landmark, // 23
    val rightHip: Landmark, // 24
    val leftKnee: Landmark, // 25
    val rightKnee: Landmark, // 26
    val leftAnkle: Landmark, // 27
    val rightAnkle: Landmark, // 28
    val leftHeel: Landmark,  // 29
    val rightHeel: Landmark, // 30
    val leftFootIndex: Landmark, // 31
    val rightFootIndex: Landmark, // 32
    // Edges
    // Angles
    val leftKneeAngle: Float,
    val rightKneeAngle: Float,
    // Distances
    val strideLength: Float
)

