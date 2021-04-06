/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.java.posedetector;

import static java.lang.Math.atan2;
import static java.lang.Math.max;
import static java.lang.Math.min;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import com.google.common.primitives.Ints;
import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.GraphicOverlay.Graphic;
import com.google.mlkit.vision.demo.InferenceInfoGraphic;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;
import java.util.List;
import java.util.Locale;

/** Draw the detected pose in preview. */
public class PoseGraphic extends Graphic {

  private static final float DOT_RADIUS = 8.0f;
  private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f;
  private static final float STROKE_WIDTH = 10.0f;
  private static final float POSE_CLASSIFICATION_TEXT_SIZE = 60.0f;

  private final Pose pose;
  private final boolean showInFrameLikelihood;
  private final boolean visualizeZ;
  private final boolean rescaleZForVisualization;
  private float zMin = Float.MAX_VALUE;
  private float zMax = Float.MIN_VALUE;

  private final List<String> poseClassification;
  private final Paint classificationTextPaint;
  private final Paint leftPaint;
  private final Paint rightPaint;
  private final Paint whitePaint;
  private static int TEXT_COLOR = Color.WHITE;
  private static float TEXT_SIZE = 60.0f;
  PoseGraphic(
      GraphicOverlay overlay,
      Pose pose,
      boolean showInFrameLikelihood,
      boolean visualizeZ,
      boolean rescaleZForVisualization,
      List<String> poseClassification
      ) {
    super(overlay);
    this.pose = pose;
    this.showInFrameLikelihood = showInFrameLikelihood;
    this.visualizeZ = visualizeZ;
    this.rescaleZForVisualization = rescaleZForVisualization;

    this.poseClassification = poseClassification;
    classificationTextPaint = new Paint();
    classificationTextPaint.setColor(Color.WHITE);
    classificationTextPaint.setTextSize(POSE_CLASSIFICATION_TEXT_SIZE);

    whitePaint = new Paint();
    whitePaint.setStrokeWidth(STROKE_WIDTH);
    whitePaint.setColor(Color.WHITE);
    whitePaint.setTextSize(IN_FRAME_LIKELIHOOD_TEXT_SIZE);
    leftPaint = new Paint();
    leftPaint.setStrokeWidth(STROKE_WIDTH);
    leftPaint.setColor(Color.GREEN);
    rightPaint = new Paint();
    rightPaint.setStrokeWidth(STROKE_WIDTH);
    rightPaint.setColor(Color.YELLOW);
  }

  @Override
  public void draw(Canvas canvas) {
    List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
    if (landmarks.isEmpty()) {
      return;
    }

    // Draw pose classification text.
    float classificationX = POSE_CLASSIFICATION_TEXT_SIZE * 0.5f;
    for (int i = 0; i < poseClassification.size(); i++) {
      float classificationY = (canvas.getHeight() - POSE_CLASSIFICATION_TEXT_SIZE * 1.5f
          * (poseClassification.size() - i));
      canvas.drawText(
          poseClassification.get(i),
          classificationX,
          classificationY,
          classificationTextPaint);
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


    if (leftHeel.getPosition().x > leftFootIndex.getPosition().x && rightHeel.getPosition().x > rightFootIndex.getPosition().x){ //왼쪽볼때
//      // Draw all the points
//      for (PoseLandmark landmark : landmarks) {
//        drawPoint(canvas, landmark, whitePaint);
//        if (visualizeZ && rescaleZForVisualization) {
//          zMin = min(zMin, landmark.getPosition3D().getZ());
//          zMax = max(zMax, landmark.getPosition3D().getZ());
//        }
//      }
      // Left body
      drawLine(canvas, leftShoulder, leftElbow, leftPaint);
      drawLine(canvas, leftElbow, leftWrist, leftPaint);
      drawLine(canvas, leftShoulder, leftHip, leftPaint);
      drawLine(canvas, leftHip, leftKnee, leftPaint);
      drawLine(canvas, leftKnee, leftAnkle, leftPaint);
      drawLine(canvas, leftWrist, leftThumb, leftPaint);
      drawLine(canvas, leftWrist, leftPinky, leftPaint);
      drawLine(canvas, leftWrist, leftIndex, leftPaint);
      drawLine(canvas, leftIndex, leftPinky, leftPaint);
      drawLine(canvas, leftAnkle, leftHeel, leftPaint);
      drawLine(canvas, leftHeel, leftFootIndex, leftPaint);
    }
    else if (leftHeel.getPosition().x < leftFootIndex.getPosition().x && rightHeel.getPosition().x < rightFootIndex.getPosition().x){ //오른쪽
      // Right body
      drawLine(canvas, rightShoulder, rightElbow, rightPaint);
      drawLine(canvas, rightElbow, rightWrist, rightPaint);
      drawLine(canvas, rightShoulder, rightHip, rightPaint);
      drawLine(canvas, rightHip, rightKnee, rightPaint);
      drawLine(canvas, rightKnee, rightAnkle, rightPaint);
      drawLine(canvas, rightWrist, rightThumb, rightPaint);
      drawLine(canvas, rightWrist, rightPinky, rightPaint);
      drawLine(canvas, rightWrist, rightIndex, rightPaint);
      drawLine(canvas, rightIndex, rightPinky, rightPaint);
      drawLine(canvas, rightAnkle, rightHeel, rightPaint);
      drawLine(canvas, rightHeel, rightFootIndex, rightPaint);
    }
    else{
      drawLine(canvas, leftShoulder, rightShoulder, whitePaint);
      drawLine(canvas, leftHip, rightHip, whitePaint);

      drawLine(canvas, leftShoulder, leftElbow, leftPaint);
      drawLine(canvas, leftElbow, leftWrist, leftPaint);
      drawLine(canvas, leftShoulder, leftHip, leftPaint);
      drawLine(canvas, leftHip, leftKnee, leftPaint);
      drawLine(canvas, leftKnee, leftAnkle, leftPaint);
      drawLine(canvas, leftWrist, leftThumb, leftPaint);
      drawLine(canvas, leftWrist, leftPinky, leftPaint);
      drawLine(canvas, leftWrist, leftIndex, leftPaint);
      drawLine(canvas, leftIndex, leftPinky, leftPaint);
      drawLine(canvas, leftAnkle, leftHeel, leftPaint);
      drawLine(canvas, leftHeel, leftFootIndex, leftPaint);
      drawLine(canvas, rightShoulder, rightElbow, rightPaint);
      drawLine(canvas, rightElbow, rightWrist, rightPaint);
      drawLine(canvas, rightShoulder, rightHip, rightPaint);
      drawLine(canvas, rightHip, rightKnee, rightPaint);
      drawLine(canvas, rightKnee, rightAnkle, rightPaint);
      drawLine(canvas, rightWrist, rightThumb, rightPaint);
      drawLine(canvas, rightWrist, rightPinky, rightPaint);
      drawLine(canvas, rightWrist, rightIndex, rightPaint);
      drawLine(canvas, rightIndex, rightPinky, rightPaint);
      drawLine(canvas, rightAnkle, rightHeel, rightPaint);
      drawLine(canvas, rightHeel, rightFootIndex, rightPaint);
    }


    boolean squat = true; //나중에 선택사항 만들어서 선택됐을 때 true로 변경
    boolean lunge = false;
    boolean situp = false;
    boolean pushup = false;
<<<<<<< HEAD

    printAngle(pose, canvas);

=======
    Paint textPaint = new Paint();
    textPaint.setColor(TEXT_COLOR);
    textPaint.setTextSize(TEXT_SIZE);
>>>>>>> 1c24bfb2558b5d89faab5394bb4002bac2f704d8
    if (squat) {
      if (leftHeel.getPosition().x > leftFootIndex.getPosition().x){ // 왼쪽을 보고있을 떼
        //무릎이 발밖으로 많이 나올경우

        if (leftKnee.getPosition().x+40 < leftFootIndex.getPosition().x || rightKnee.getPosition().x + 40 < rightFootIndex.getPosition().x) {
          canvas.drawText("무릎을 넣어주세요",
                  translateX(150),
                  translateY(150),
                  textPaint);
        }
        //무릎과 엉덩이의 각도가 90도보다 작아지게 제대로 앉지 않았을 경우
        else if(leftKnee.getPosition().y > leftHip.getPosition().y+50 || rightKnee.getPosition().y > rightHip.getPosition().y) {
          canvas.drawText("더 앉아주세요",
                  translateX(150),
                  translateY(150),
                  textPaint);
        }
      }
      else { //오른쪽 보고 있을 때
        //무릎이 발밖으로 많이 나올경우
        if (leftKnee.getPosition().x > leftFootIndex.getPosition().x + 40 || rightKnee.getPosition().x > rightFootIndex.getPosition().x + 40) {
          canvas.drawText("무릎을 넣어주세요",
                  translateX(150),
                  translateY(150),
                  textPaint);
        }
        //무릎과 엉덩이의 각도가 90도보다 작아지게 제대로 앉지 않았을 경우
        else if (leftKnee.getPosition().y > leftHip.getPosition().y+50 || rightKnee.getPosition().y > rightHip.getPosition().y) {
          canvas.drawText("더 앉아주세요",
                  translateX(150),
                  translateY(150),
                  textPaint);
        }
      }
    }

    if(lunge) {
      //상체가 기울어질 경우
      if (Math.abs(leftShoulder.getPosition().x - leftHip.getPosition().x) > 10 || Math.abs(rightShoulder.getPosition().x - rightHip.getPosition().x) > 10) {
        canvas.drawText("상체를 똑바로 세워주세요 ",
                translateX(150),
                translateY(150),
                textPaint);
      }

      //무릎이 발밖으로 많이 나올경우
      else if (leftKnee.getPosition().x + 20 < leftFootIndex.getPosition().x || rightKnee.getPosition().x + 20 < rightFootIndex.getPosition().x) {
        canvas.drawText("무릎을 넣어주세요",
                translateX(150),
                translateY(150),
                textPaint);
      }

      // 뒷발의 무릎이 지면에 거의 닿을정도로 앉아야한다.
      // 앞발 뒷발 구분
      else {
        if (leftFootIndex.getPosition().x < rightFootIndex.getPosition().x) { //왼발이 앞에 있을 때
          if (rightHeel.getPosition().y > rightKnee.getPosition().y) {
            canvas.drawText("더 내려가주세요",
                    translateX(150),
                    translateY(150),
                    textPaint);
          }
        } else { // 오른발이 앞에 있을 때
          if (leftHeel.getPosition().y > leftKnee.getPosition().y) {
            canvas.drawText("더 내려가주세요",
                    translateX(150),
                    translateY(150),
                    textPaint);
          }
        }
      }
    }

    if(situp){
      //팔꿈치가 무릎에 닿을 정도로 올라와야함
      if(Math.abs(leftElbow.getPosition().x - leftKnee.getPosition().x) > 30 || Math.abs(rightElbow.getPosition().x - rightKnee.getPosition().x) > 30){
        canvas.drawText("더 올라와주세요",
                translateX(150),
                translateY(150),
                textPaint);
      }
      //발이 바닥과 떨어지지 않게 한다
      else if(Math.abs(leftHeel.getPosition().y - leftHip.getPosition().y)>30 || Math.abs(rightHeel.getPosition().y - rightHip.getPosition().y)>30){
        canvas.drawText("발을 땅에 붙여주세요",
                translateX(150),
                translateY(150),
                textPaint);
      }
    }

    if(pushup){
      //내려 갔을 때 몸이 일자가 되어야함 (엉덩이가 내려가거나 올라가면 안됨)
      if(Math.abs(leftShoulder.getPosition().y - leftHip.getPosition().y) > 30 || Math.abs(rightShoulder.getPosition().y - rightHip.getPosition().y) > 30){
        canvas.drawText("몸을 일자로 곧게 펴주세요",
                translateX(150),
                translateY(150),
                textPaint);
      }

      // 몸이 바닥에 닫기 직전까지 내려야 한다.
      else if(Math.abs(leftShoulder.getPosition().y - leftWrist.getPosition().y) > 30 || Math.abs(rightShoulder.getPosition().y - rightWrist.getPosition().y) > 30){
        canvas.drawText("더 내려가주세요",
                translateX(150),
                translateY(150),
                textPaint);
      }
    }

    
    // Draw inFrameLikelihood for all points
    if (showInFrameLikelihood) {
      for (PoseLandmark landmark : landmarks) {
        canvas.drawText(
            String.format(Locale.US, "%.2f", landmark.getInFrameLikelihood()),
            translateX(landmark.getPosition().x),
            translateY(landmark.getPosition().y),
            whitePaint);
      }
    }
  }


  void drawPoint(Canvas canvas, PoseLandmark landmark, Paint paint) {
    PointF point = landmark.getPosition();
    canvas.drawCircle(translateX(point.x), translateY(point.y), DOT_RADIUS, paint);
    canvas.drawCircle(translateX(point.x), translateY(point.y), DOT_RADIUS, paint);
  }

  void drawLine(Canvas canvas, PoseLandmark startLandmark, PoseLandmark endLandmark, Paint paint) {
    // When visualizeZ is true, sets up the paint to draw body line in different colors based on
    // their z values.
    if (visualizeZ) {
      PointF3D start = startLandmark.getPosition3D();
      PointF3D end = endLandmark.getPosition3D();

      // Gets the range of z value.
      float zLowerBoundInScreenPixel;
      float zUpperBoundInScreenPixel;

      if (rescaleZForVisualization) {
        zLowerBoundInScreenPixel = min(-0.001f, scale(zMin));
        zUpperBoundInScreenPixel = max(0.001f, scale(zMax));
      } else {
        // By default, assume the range of z value in screen pixel is [-canvasWidth, canvasWidth].
        float defaultRangeFactor = 1f;
        zLowerBoundInScreenPixel = -defaultRangeFactor * canvas.getWidth();
        zUpperBoundInScreenPixel = defaultRangeFactor * canvas.getWidth();
      }

      // Gets average z for the current body line
      float avgZInImagePixel = (start.getZ() + end.getZ()) / 2;
      float zInScreenPixel = scale(avgZInImagePixel);

      if (zInScreenPixel < 0) {
        // Sets up the paint to draw the body line in red if it is in front of the z origin.
        // Maps values within [zLowerBoundInScreenPixel, 0) to [255, 0) and use it to control the
        // color. The larger the value is, the more red it will be.
        int v = (int) (zInScreenPixel / zLowerBoundInScreenPixel * 255);
        v = Ints.constrainToRange(v, 0, 255);
        paint.setARGB(255, 255, 255 - v, 255 - v);
      } else {
        // Sets up the paint to draw the body line in blue if it is behind the z origin.
        // Maps values within [0, zUpperBoundInScreenPixel] to [0, 255] and use it to control the
        // color. The larger the value is, the more blue it will be.
        int v = (int) (zInScreenPixel / zUpperBoundInScreenPixel * 255);
        v = Ints.constrainToRange(v, 0, 255);
        paint.setARGB(255, 255 - v, 255 - v, 255);
      }

      canvas.drawLine(
          translateX(start.getX()),
          translateY(start.getY()),
          translateX(end.getX()),
          translateY(end.getY()),
          paint);

    } else {
      PointF start = startLandmark.getPosition();
      PointF end = endLandmark.getPosition();
      canvas.drawLine(
          translateX(start.x), translateY(start.y), translateX(end.x), translateY(end.y), paint);
    }
  }
  double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
    double result;
    try {
      result =
              Math.toDegrees(
                      atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                              lastPoint.getPosition().x - midPoint.getPosition().x)
                              - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                              firstPoint.getPosition().x - midPoint.getPosition().x));
      result = Math.abs(result);
      if (result > 180) {
        result = (360.0 - result);
      }
    } catch (Exception e){
      result = -1.f;
    }

    return result;
  }
  void printAngle(Pose pose, Canvas canvas) {

    float text_size = 30.0f;
    float x = text_size * 0.5f;
    float y = 1200.f;

    double rightHipAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
            pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE));
    double leftHipAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
            pose.getPoseLandmark(PoseLandmark.LEFT_KNEE));
    double rightKneeAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
            pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE),
            pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE));
    double leftKneeAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
            pose.getPoseLandmark(PoseLandmark.LEFT_KNEE),
            pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE));
    double rightShoulderAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.RIGHT_HIP));
    double leftShoulderAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.LEFT_HIP));
    double rightElbowAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST),
            pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER));
    double leftElbowAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_WRIST),
            pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER));
    double rightAnkleAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE),
            pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE),
            pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX));
    double leftAnkleAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_KNEE),
            pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE),
            pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX));
    canvas.drawText("leftShoulder: " + leftShoulderAngle, x, y + text_size * 0, whitePaint);
    canvas.drawText("rightShoulder: " + rightShoulderAngle, x, y  + text_size * 1, whitePaint);
    canvas.drawText("leftElbow: " + leftElbowAngle, x, y + text_size * 2, whitePaint);
    canvas.drawText("rightElbow: " + rightElbowAngle, x, y  + text_size * 3, whitePaint);
    canvas.drawText("leftHip: " + leftHipAngle, x, y + text_size * 4, whitePaint);
    canvas.drawText("rightHip: " + rightHipAngle, x, y + text_size * 5, whitePaint);
    canvas.drawText("leftKnee: " + leftKneeAngle, x, y + text_size * 6, whitePaint);
    canvas.drawText("rightKnee: " + rightKneeAngle, x, y + text_size * 7, whitePaint);
    canvas.drawText("leftAnkle: " + leftAnkleAngle, x, y + text_size * 8, whitePaint);
    canvas.drawText("rightAnkle: " + rightAnkleAngle, x, y + text_size * 9, whitePaint);


  }
}
