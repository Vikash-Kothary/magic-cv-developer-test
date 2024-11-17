# DATA SCIENTIST ASSESSMENT

This test is designed to assess your ability to analyse real-time data output from the MediaPipe 
computer vision library.

MediaPipe performs pose analysis by converting a video feed of a person into a stream of 33 3D 
coordinates, representing points on the person’s body. You will need to create algorithms
that analyse this stream of pose data to determine how well the subject performs the following
exercise: 

**Alternating Lunge**

## INSTRUCTIONS

1. Download and setup Android Studio.
2. Download the app starter code.
3. ExerciseRepCounterImpl is where you will need to create your algorithms for analysing
   the exercise being performed in the video linked above. Use the functions
   incrementRepCount() and sendProgressUpdate() to update the app UI with the output of
   your algorithms which must:
    a. Count exercise repetitions.
    b. Analyse the progress of the current movement. The progress bar should fill
   gradually as the user performs the movement, and reduce to 0 as the user
   returns to the start position. Employ the appropriate techniques to handle any
   fluctuations in the data to ensure smooth progress updates.
   MediaPipe landmark detection guide docs

## SUBMISSION

Please submit a link to a public Git repository containing all files. Your submission is to be emailed to: engineering@magic.fit