/*
 * Copyright (C) 2009,2010,2011,2012 Samuel Audet
 *
 * This file is part of JavaCV.
 *
 * JavaCV is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version (subject to the "Classpath" exception
 * as provided in the LICENSE.txt file that accompanied this code).
 *
 * JavaCV is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JavaCV.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.bytedeco.javacpp;

import java.nio.DoubleBuffer;
import java.util.Arrays;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.MemberSetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;

import static org.bytedeco.javacpp.Loader.*;
import static org.bytedeco.javacpp.opencv_core.*;

/**
 *
 * @author Samuel Audet
 */
@Properties(inherit=opencv_core.class, value={
    @Platform(define={"MAX_SIZE 16", "CV_INLINE static inline"}, include="cvkernels.h", compiler="fastfpu") })
public class cvkernels {
    static { load(); }

    public static class KernelData extends Pointer {
        static { load(); }
        public KernelData() { allocate(); }
        public KernelData(int size) { allocateArray(size); }
        public KernelData(Pointer p) { super(p); }
        private native void allocate();
        private native void allocateArray(int size);

        @Override public KernelData position(int position) {
            return (KernelData)super.position(position);
        }

        // input
        public native IplImage srcImg();         public native KernelData srcImg(IplImage srcImg);
        public native IplImage srcImg2();        public native KernelData srcImg2(IplImage srcImg2);
        public native IplImage subImg();         public native KernelData subImg(IplImage subImg);
        public native IplImage srcDotImg();      public native KernelData srcDotImg(IplImage srcDotImg);
        public native IplImage mask();           public native KernelData mask(IplImage mask);
        public native double zeroThreshold();    public native KernelData zeroThreshold(double zeroThreshold);
        public native double outlierThreshold(); public native KernelData outlierThreshold(double outlierThreshold);
        public native CvMat H1();                public native KernelData H1(CvMat H1);
        public native CvMat H2();                public native KernelData H2(CvMat H2);
        public native CvMat X();                 public native KernelData X (CvMat X);

        // output
        public native IplImage transImg();       public native KernelData transImg(IplImage transImg);
        public native IplImage dstImg();         public native KernelData dstImg(IplImage dstImg);
        public native int dstCount();            public native KernelData dstCount(int dstCount);
        public native int dstCountZero();        public native KernelData dstCountZero(int dstCountZero);
        public native int dstCountOutlier();     public native KernelData dstCountOutlier(int dstCountOutlier);
        public native double srcDstDot();        public native KernelData srcDstDot(double srcDstDot);
//        public native DoublePointer dstDstDot(); public native KernelData dstDstDot(DoublePointer dstDstDot);

        // Hack to let us use DoubleBuffer directly instead of DoublePointer, which also
        // provides us with Java references to boot, keeping the garbage collector happy
        private native @MemberSetter @Name("dstDstDot") KernelData setDstDstDot(DoubleBuffer dstDstDot);
        private DoubleBuffer[] dstDstDotBuffers = new DoubleBuffer[1];
        public DoubleBuffer dstDstDot() {
            return dstDstDotBuffers[position];
        }
        public KernelData dstDstDot(DoubleBuffer dstDstDot) {
            if (dstDstDotBuffers.length < capacity) {
                dstDstDotBuffers = Arrays.copyOf(dstDstDotBuffers, capacity);
            }
            dstDstDotBuffers[position] = dstDstDot;
            return setDstDstDot(dstDstDot);
        }

        private native @Name("operator=") @ByRef KernelData put(@ByRef KernelData x);
    }

    public static native void multiWarpColorTransform32F(KernelData data, int size, CvRect roi, CvScalar fillColor);
    public static native void multiWarpColorTransform8U(KernelData data, int size, CvRect roi, CvScalar fillColor);
}
