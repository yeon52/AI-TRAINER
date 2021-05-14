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
import android.speech.tts.TextToSpeech;

import com.google.common.primitives.Ints;
import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.GraphicOverlay.Graphic;
import com.google.mlkit.vision.demo.InferenceInfoGraphic;
import com.google.mlkit.vision.demo.java.LivePreviewActivity;
import com.google.mlkit.vision.demo.preference.UserInterface;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.List;
import java.util.Locale;

/**
 * Draw the detected pose in preview.
 */
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
  private TextToSpeech tts1;

  private final List<String> poseClassification;
  private final Paint classificationTextPaint;
  private final Paint leftPaint;
  private final Paint rightPaint;
  private final Paint whitePaint;
  private static int TEXT_COLOR = Color.WHITE;
  private static float TEXT_SIZE = 60.0f;
  static int cnt_squat = 0;
  static int linecheck_pushup = 0;
  static int knee_check = 0;
  static int situp_check = 0;
  static double min_left_elbow = 999;
  static double min_left_Knee = 999;
  static int start_pushup = 0;
  static int cnt_pushup = 0;
  static int situp_cnt =0;
  static int up_check=0;
  static double min_leftHip = 999;
  static int lunge_knee = 0;
  static int lunge_tilt = 0;
  static int lunge_depth = 0;
  static double min_left_knee_angle = 190.f;
  static double min_right_knee_angle = 190.f;
  static double min_hip = 0.f;
  static double min_left_knee;
  static double min_right_knee;
  static double LUNGE_THRESHOLD = 40.f;
  static double left_knee_toe_dist;
  static double right_knee_toe_dist;
  static int chk_lunge = 0;

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
    tts1 = LivePreviewActivity.tts;
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


    if (leftHeel.getPosition().x > leftFootIndex.getPosition().x && rightHeel.getPosition().x > rightFootIndex.getPosition().x) { //왼쪽볼때
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
    } else if (leftHeel.getPosition().x < leftFootIndex.getPosition().x && rightHeel.getPosition().x < rightFootIndex.getPosition().x) { //오른쪽
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
    } else {
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

    /*
    System.out.println("왼쪽 어깨 : " + leftShoulder.getPosition());
    System.out.println("오른쪽 어깨 : " + rightShoulder.getPosition());
    System.out.println("왼쪽 팔꿈치 : " + leftElbow.getPosition());
    System.out.println("오른쪽 팔꿈치 : " + rightElbow.getPosition());
    System.out.println("왼쪽 손목 : " + leftWrist.getPosition());
    System.out.println("오른쪽 손목 : " + rightWrist.getPosition());
    System.out.println("왼쪽 엉덩이 : " + leftHip.getPosition());
    System.out.println("오른쪽 엉덩이 : " + rightHip.getPosition());
    System.out.println("왼쪽 무릎 : " + leftKnee.getPosition());
    System.out.println("오른쪽 무릎 : " + rightKnee.getPosition());
    System.out.println("왼쪽 발목 : " + leftAnkle.getPosition());
    System.out.println("오른쪽 발목 : " + rightAnkle.getPosition());
    System.out.println();

    System.out.println("왼쪽 새끼 손가락 : " + leftPinky.getPosition());
    System.out.println("오른쪽 새끼 손가락 : " + rightPinky.getPosition());
    System.out.println("왼쪽 손가락 : " + leftIndex.getPosition());
    System.out.println("오른쪽 손가락 : " + rightIndex.getPosition());
    System.out.println("왼쪽 엄지 : " + leftThumb.getPosition());
    System.out.println("오른쪽 엄지 : " + rightThumb.getPosition());
    System.out.println("왼쪽 발꿈치 : " + leftHeel.getPosition());
    System.out.println("오른쪽 발꿈치 : " + rightHeel.getPosition());
    System.out.println("왼쪽 발꿈치 : " + leftFootIndex.getPosition());
    System.out.println("오른쪽 발꿈치 : " + rightFootIndex.getPosition());
    System.out.println();
    */

    boolean squat = ((UserInterface) UserInterface.context_interface).squat; //나중에 선택사항 만들어서 선택됐을 때 true로 변경
    boolean lunge = ((UserInterface) UserInterface.context_interface).lunge;
    boolean situp = ((UserInterface) UserInterface.context_interface).situp;
    boolean pushup = ((UserInterface) UserInterface.context_interface).pushup;
    printAngle(pose, canvas);

    Paint textPaint = new Paint();
    textPaint.setColor(TEXT_COLOR);
    textPaint.setTextSize(TEXT_SIZE);

    if (squat) {
      //사각형 틀 그리기
//      Paint paint = new Paint();
//      paint.setColor(Color.GREEN);
//      canvas.drawRect(300, 250, 1120, 300, paint);
//      canvas.drawRect(300, 1950, 1120, 2000, paint);
//     canvas.drawText("knee angle: " + min_left_Knee, 100, 100, whitePaint);

      canvas.drawText("횟수: " + cnt_squat, 50, 100, textPaint);

      if (leftKneeAngle < min_left_Knee && leftKneeAngle < 130)
        min_left_Knee = leftKneeAngle; //앉았을때 각도

      if (rightKneeAngle >= 160) {  //서있을 때
        if(min_left_Knee > 65 && min_left_Knee!=999 && !tts1.isSpeaking()){
          tts1.speak("더 앉아주세요", TextToSpeech.QUEUE_FLUSH, null);
        }

        else if(knee_check == 0 && min_left_Knee <= 65 && !tts1.isSpeaking()){ //무릎이 발밖으로 안나오고 잘앉았을때 카운트
          cnt_squat++;
          tts1.speak(String.valueOf(cnt_squat), TextToSpeech.QUEUE_FLUSH, null);
        }
        knee_check = 0;
        min_left_Knee = 999;
      }
      if (leftHeel.getPosition().x > leftFootIndex.getPosition().x) { // 왼쪽을 보고있을 때
        //무릎이 발밖으로 많이 나올경우
        if (knee_check == 0 && leftKnee.getPosition().x+55 < leftFootIndex.getPosition().x) {
          if(!tts1.isSpeaking()) { //현재 말하고 있는게 없다면
            knee_check=1;
            tts1.speak("무릎을 넣어주세요.", TextToSpeech.QUEUE_FLUSH, null);
          }
        }
      } else { //오른쪽 보고 있을 때
        //무릎이 발밖으로 많이 나올경우
        if (knee_check == 0 && rightKnee.getPosition().x > rightFootIndex.getPosition().x + 55) {
          if(!tts1.isSpeaking()) { //현재 말하고 있는게 없다면
            knee_check = 1;
            tts1.speak("무릎을 넣어주세요.", TextToSpeech.QUEUE_FLUSH, null);
          }
        }
      }
    }

    if (lunge) {
      //사각형 틀 그리기
      Paint paint = new Paint();
      paint.setColor(Color.GREEN);
//      canvas.drawRect(300, 250, 1120, 300, paint);
//      canvas.drawRect(300, 1950, 1120, 2000, paint);

      centerBodyTilt = 0.5f * rightBodyTilt + 0.5f * leftBodyTilt;
      centerHip = 0.5f * rightHip.getPosition().y + 0.5f * leftHip.getPosition().y;

      if(min_left_knee > leftKneeAngle) min_left_knee_angle = leftKneeAngle;
      if(min_right_knee > rightKneeAngle) min_right_knee_angle = rightKneeAngle;
      if(min_hip < centerHip) {
        min_hip = centerHip;
        min_left_knee = leftKnee.getPosition().y;
        min_right_knee = rightKnee.getPosition().y;
        left_knee_toe_dist = leftKnee.getPosition().x - leftFootIndex.getPosition().x;
        right_knee_toe_dist = rightKnee.getPosition().x - rightFootIndex.getPosition().x;
      }

      if (rightKneeAngle > 150.f || leftKneeAngle > 150.f) {
        if(chk_lunge == 1) {
          if(lunge_tilt == 1) tts1.speak("상체를 똑바로 세워주세요.",TextToSpeech.QUEUE_ADD,null);
          if(left_knee_toe_dist * right_knee_toe_dist > 0) tts1.speak("무릎을 넣어주세요.", TextToSpeech.QUEUE_ADD, null);
          if(min_hip + LUNGE_THRESHOLD < min_left_knee && min_hip + LUNGE_THRESHOLD < min_right_knee) tts1.speak("더 내려가주세요.", TextToSpeech.QUEUE_ADD, null);
        }
        lunge_knee = 0;
        lunge_tilt = 0;
        lunge_depth = 0;
        chk_lunge = 0;
        min_right_knee_angle = 160.f;
        min_left_knee_angle = 160.f;
        min_left_knee = 0.f;
        min_right_knee = 0.f;
        min_hip = 0.f;
      }
      else {
        chk_lunge = 1;
        //상체가 기울어질 경우
        if (lunge_tilt == 0 && (70.f >= centerBodyTilt || centerBodyTilt >= 110.f)) {
          lunge_tilt = 1;
        }
      }
    }

    if(situp) {
      //사각형 틀 그리기
      Paint paint = new Paint();
      paint.setColor(Color.GREEN);
      canvas.drawRect(250, 300, 300, 1120, paint);
      canvas.drawRect(2050, 300, 2100, 1120, paint);

      canvas.drawText("횟수: " + situp_cnt, 50, 100, textPaint);

      if (leftFootIndex.getPosition().x < leftKnee.getPosition().x) {  // 몸의 왼쪽 편이 보이도록 누워있을 때
        if (Math.abs(leftShoulder.getPosition().y - leftHip.getPosition().y) < 30) {  // 누워있을 때
          if(situp_check > 0) { //제대로 잘 동작하고 다시 내려왔을 때 (다시 누웠을 때)
            //싯업 카운트하기
            situp_cnt++;
            tts1.speak(String.valueOf(situp_cnt), TextToSpeech.QUEUE_FLUSH, null);
            tts1.playSilence(2000, TextToSpeech.QUEUE_ADD, null);
            situp_check = 0;  //팔꿈치를 무릎에 찍었는지 확인하는 변수 (제대로 잘 올라간건지 확인하는 변수)
            min_leftHip = 999;
          }
          if(!tts1.isSpeaking() && min_leftHip > 100 && up_check ==1) { //최소값이 100도보다 클때 (제대로 못한 경우)
            //조금이라도 올라갔지만 최소값이 100보다 크고, 말을 안하는 상황일때
            tts1.speak("더 올라와주세요", TextToSpeech.QUEUE_FLUSH, null);
            tts1.playSilence(2000, TextToSpeech.QUEUE_ADD, null);
            min_leftHip = 999;
            up_check = 0;
          }
        } else {
          up_check = 1; //조금이라도 올라갔을 경우
        }

        //팔꿈치가 무릎에 닿을 정도로 올라와야함
        if (50 >= Math.abs(leftElbow.getPosition().x - leftKnee.getPosition().x)) {
          //잘 한 경우
          situp_check++;
        } else if ((50 < Math.abs(leftElbow.getPosition().x - leftKnee.getPosition().x)) && (Math.abs(leftElbow.getPosition().x - leftKnee.getPosition().x) <= 150)) {
          if(leftHipAngle < min_leftHip) {
            min_leftHip = leftHipAngle; //최소값 기록
          }
        }
      }

      else { // 몸의 오른쪽 편이 보이도록 누워있을 때
        if (Math.abs(rightShoulder.getPosition().y - rightHip.getPosition().y) < 30) {  // 누워있을 때
          if(situp_check > 0) { //제대로 잘 동작하고 다시 내려왔을 때 (다시 누웠을 때)
            //싯업 카운트하기
            situp_cnt++;
            tts1.speak(String.valueOf(situp_cnt), TextToSpeech.QUEUE_FLUSH, null);
            tts1.playSilence(2000, TextToSpeech.QUEUE_ADD, null);
            situp_check = 0;  //팔꿈치를 무릎에 찍었는지 확인하는 변수 (제대로 잘 올라간건지 확인하는 변수)
            min_leftHip = 999;
          }
          if(!tts1.isSpeaking() && min_leftHip > 100 && up_check ==1) { //최소값이 100도보다 클때 (제대로 못한 경우)
            //조금이라도 올라갔지만 최소값이 100보다 크고, 말을 안하는 상황일때
            tts1.speak("더 올라와주세요", TextToSpeech.QUEUE_FLUSH, null);
            tts1.playSilence(2000, TextToSpeech.QUEUE_ADD, null);
            min_leftHip = 999;
            up_check = 0;
          }
        } else {
          up_check = 1; //조금이라도 올라갔을 경우
        }

        //팔꿈치가 무릎에 닿을 정도로 올라와야함
        if (50 >= Math.abs(rightElbow.getPosition().x - rightKnee.getPosition().x)) {
          //잘 한 경우
          situp_check++;
        } else if ((50 < Math.abs(rightElbow.getPosition().x - rightKnee.getPosition().x)) && (Math.abs(rightElbow.getPosition().x - rightKnee.getPosition().x) <= 150)) {
          if(leftHipAngle < min_leftHip) {
            min_leftHip = leftHipAngle; //최소값 기록
          }
        }
      }
    }

    if (pushup) {
      //사각형 틀 그리기
//      Paint paint = new Paint();
//      paint.setColor(Color.GREEN);
//      canvas.drawRect(250, 300, 300, 1120, paint);
//      canvas.drawRect(2050, 300, 2100, 1120, paint);
      //canvas.drawText("minElbow: " + min_left_elbow, 100, 100, whitePaint);
      //canvas.drawText("chk_pushup: " + chk_pushup, 100, 200, whitePaint);
      //canvas.drawText("start_pushup: " + start_pushup, 100, 300, whitePaint);
      //canvas.drawText("chk_line: " + linecheck_pushup, 100, 100, whitePaint);

      canvas.drawText("횟수: " + cnt_pushup, 50, 100, textPaint);

      if (leftElbowAngle < min_left_elbow && leftElbowAngle < 130)
        min_left_elbow = leftElbowAngle; //상체가 최대 내려간 정도

      else if (leftElbowAngle > 140) { //올라와있을 때(올라가는중)

        if (leftHipAngle <= 150 || leftHipAngle > 180 && start_pushup==1) {//내려 갔을 때 몸이 일자가 되어야함 (엉덩이가 내려가거나 올라가면 안됨)
          if(!tts1.isSpeaking() && linecheck_pushup == 0) { //현재 말하고 있는게 없다면
            tts1.speak("몸을 일자로 곧게 펴주세요.", TextToSpeech.QUEUE_FLUSH, null);
            linecheck_pushup = 1; //한번말했으면 말안하게
          }
        }
        else if (min_left_elbow > 90 && min_left_elbow != 999) { //각도가 90도 밑으로 내려가지 않으면
          if(!tts1.isSpeaking()) { //현재 말하고 있는게 없다면
            tts1.speak("더 내려가주세요.", TextToSpeech.QUEUE_FLUSH, null);
          }
        }
        else if(start_pushup==1 && min_left_elbow != 999){ //개수 세기
          if(!tts1.isSpeaking()) { //현재 말하고 있는게 없다면
            cnt_pushup++;
            tts1.speak(String.valueOf(cnt_pushup), TextToSpeech.QUEUE_FLUSH, null);
            linecheck_pushup = 0; //라인체크 초기화
          }
        }
        min_left_elbow = 999; //올라오면 각도 다시 초기화
        start_pushup = 0;

      } else { //내려갈때
        start_pushup = 1;
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
  double getTilt(PoseLandmark firstPoint, PoseLandmark secondPoint) {
    double result;
    try {
      result = Math.toDegrees(
              atan2(secondPoint.getPosition().y - firstPoint.getPosition().y,
                      secondPoint.getPosition().x - firstPoint.getPosition().x));
      result = Math.abs(result);
      if (result > 180.f) {
        result = (360.f - result);
      }
    } catch (Exception e) {
      result = -1.f;
    }
    return result;
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
    } catch (Exception e) {
      result = -1.f;
    }

    return result;
  }

  double rightHipAngle;
  double leftHipAngle;
  double rightKneeAngle;
  double leftKneeAngle;
  double rightShoulderAngle;
  double leftShoulderAngle;
  double rightElbowAngle;
  double leftElbowAngle;
  double rightAnkleAngle;
  double leftAnkleAngle;
  double rightBodyTilt;
  double leftBodyTilt;
  double centerBodyTilt;
  double centerHip;

  void printAngle(Pose pose, Canvas canvas) {
    float text_size = 30.0f;
    float x = text_size * 0.5f;
    float y = 400.f;

    rightHipAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
            pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE));
    leftHipAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
            pose.getPoseLandmark(PoseLandmark.LEFT_KNEE));
    rightKneeAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
            pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE),
            pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE));
    leftKneeAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
            pose.getPoseLandmark(PoseLandmark.LEFT_KNEE),
            pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE));
    rightShoulderAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.RIGHT_HIP));
    leftShoulderAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.LEFT_HIP));
    rightElbowAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST),
            pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER));
    leftElbowAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_WRIST),
            pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER));
    rightAnkleAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE),
            pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE),
            pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX));
    leftAnkleAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.LEFT_KNEE),
            pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE),
            pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX));
    rightBodyTilt = getTilt(pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER), pose.getPoseLandmark(PoseLandmark.RIGHT_HIP));
    leftBodyTilt = getTilt(pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER), pose.getPoseLandmark(PoseLandmark.LEFT_HIP));

        /*
        canvas.drawText("leftShoulder: " + leftShoulderAngle, x, y + text_size * 0, whitePaint);
        canvas.drawText("rightShoulder: " + rightShoulderAngle, x, y + text_size * 1, whitePaint);
        canvas.drawText("leftElbow: " + leftElbowAngle, x, y + text_size * 2, whitePaint);
        canvas.drawText("rightElbow: " + rightElbowAngle, x, y + text_size * 3, whitePaint);
        canvas.drawText("leftHip: " + leftHipAngle, x, y + text_size * 4, whitePaint);
        canvas.drawText("rightHip: " + rightHipAngle, x, y + text_size * 5, whitePaint);
        canvas.drawText("leftKnee: " + leftKneeAngle, x, y + text_size * 6, whitePaint);
        canvas.drawText("rightKnee: " + rightKneeAngle, x, y + text_size * 7, whitePaint);
        canvas.drawText("leftAnkle: " + leftAnkleAngle, x, y + text_size * 8, whitePaint);
        canvas.drawText("rightAnkle: " + rightAnkleAngle, x, y + text_size * 9, whitePaint);
*/

  }
}