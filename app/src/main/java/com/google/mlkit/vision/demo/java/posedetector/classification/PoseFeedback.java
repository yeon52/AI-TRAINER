package com.google.mlkit.vision.demo.java.posedetector.classification;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import static java.lang.Math.atan2;
public class PoseFeedback {
    private Pose pose;
    PoseFeedback(Pose pose) {
        this.pose = pose;
    }

    PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
    PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
    PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
    PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
    PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
    PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
    PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
    PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
    PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
    PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
    PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
    PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

    PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
    PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
    PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
    PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
    PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
    PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
    PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
    PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
    PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
    PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);

//    double rightHipAngle = getAngle(rightShoulder, rightHip, rightKnee);
//    double leftHipAngle = getAngle(leftShoulder, leftHip, leftKnee);
//    double rightKneeAngle = getAngle(rightHip, rightKnee, rightAnkle);
//    double leftKneeAngle = getAngle(leftHip, leftKnee, leftAnkle);
//    double rightShoulderAngle = getAngle(rightElbow, rightShoulder, rightHip);
//    double leftShoulderAngle = getAngle(leftElbow, leftShoulder, leftHip);
//    double rightElbowAngle = getAngle(rightWrist, rightElbow, rightShoulder);
//    double leftElbowAngle = getAngle(leftWrist, leftElbow, leftShoulder);
//    double rightAnkleAngle = getAngle(rightKnee, rightAnkle, rightFootIndex);
//    double leftAnkleAngle = getAngle(leftKnee, leftAnkle, leftFootIndex);
//
//    public static double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
//        double result;
//        try {
//            result =
//                    Math.toDegrees(
//                            atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
//                                    lastPoint.getPosition().x - midPoint.getPosition().x)
//                                    - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
//                                    firstPoint.getPosition().x - midPoint.getPosition().x));
//            result = Math.abs(result);
//            if (result > 180) {
//                result = (360.0 - result);
//            }
//        } catch (Exception e){
//            result = -1.f;
//        }
//
//        return result;
//    }
}
