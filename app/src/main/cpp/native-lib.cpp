#include <jni.h>
#include <string>


#include <opencv2/core/core.hpp>

#include <opencv2/highgui/highgui.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/imgproc/imgproc.hpp>





#include <vector>



extern "C" JNIEXPORT jstring JNICALL
Java_com_myeyes_myeyes_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hola desde  C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_myeyes_myeyes_MainActivity_validate(JNIEnv *env, jobject instance, jlong matAddrGr,
                                             jlong matAddrRgba) {

    cv::Rect();
    cv::Mat();
    std::string hello2 = "Hola desde vaidate";
    return env->NewStringUTF(hello2.c_str());
}


extern "C"
JNIEXPORT void JNICALL
Java_com_myeyes_myeyes_MainActivity_grises(JNIEnv *env, jobject instance, jlong imgMat) {
    cv::Mat &src = * ((cv::Mat *) imgMat);

    // src is RGBA
    cvtColor(src, src, CV_BGR2GRAY);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_myeyes_myeyes_MainActivity_disparityMap(JNIEnv *env, jobject instance, jlong imgLeft,
                                                 jlong imgRight) {

    cv::Mat &left = * ((cv::Mat *) imgLeft);
    cv::Mat &right = * ((cv::Mat *) imgRight);




}