package com.ss.editor.util;

import static java.lang.Thread.currentThread;
import com.jme3.math.*;
import com.ss.editor.EditorThread;
import org.jetbrains.annotations.NotNull;
import rlib.util.CycleBuffer;

/**
 * The container with local objects.
 *
 * @author JavaSaBr.
 */
public class LocalObjects {

    private static final int SIZE = 50;

    @NotNull
    public static LocalObjects get() {
        return ((EditorThread) currentThread()).getLocal();
    }

    /**
     * The buffer of vectors.
     */
    @NotNull
    private final CycleBuffer<Vector3f> vectorBuffer;

    /**
     * The buffer of vectors.
     */
    @NotNull
    private final CycleBuffer<Vector2f> vector2fBuffer;

    /**
     * The buffer of planes.
     */
    @NotNull
    private final CycleBuffer<Plane> planeBuffer;

    /**
     * The buffer of colors.
     */
    @NotNull
    private final CycleBuffer<ColorRGBA> colorBuffer;

    /**
     * The buffer of rotation.
     */
    @NotNull
    private final CycleBuffer<Quaternion> rotationBuffer;

    /**
     * The buffer of rays.
     */
    @NotNull
    private final CycleBuffer<Ray> rayBuffer;

    /**
     * The buffer of matrixes.
     */
    @NotNull
    private final CycleBuffer<Matrix3f> matrix3fBuffer;

    /**
     * The buffer of matrix float arrays.
     */
    @NotNull
    private final CycleBuffer<float[]> matrixFloatBuffer;

    @SuppressWarnings("unchecked")
    public LocalObjects() {
        this.vectorBuffer = new CycleBuffer<>(Vector3f.class, SIZE, Vector3f::new);
        this.vector2fBuffer = new CycleBuffer<>(Vector2f.class, SIZE, Vector2f::new);
        this.planeBuffer = new CycleBuffer<>(Plane.class, SIZE, Plane::new);
        this.rotationBuffer = new CycleBuffer<>(Quaternion.class, SIZE, Quaternion::new);
        this.rayBuffer = new CycleBuffer<>(Ray.class, SIZE, Ray::new);
        this.matrix3fBuffer = new CycleBuffer<>(Matrix3f.class, SIZE, Matrix3f::new);
        this.matrixFloatBuffer = new CycleBuffer<>(float[].class, SIZE, () -> new float[16]);
        this.colorBuffer = new CycleBuffer<>(ColorRGBA.class, SIZE, ColorRGBA::new);
    }

    /**
     * @return the next free matrix.
     */
    @NotNull
    public Matrix3f nextMatrix3f() {
        return matrix3fBuffer.next();
    }

    /**
     * @return the next free matrix float array.
     */
    @NotNull
    public float[] nextMatrixFloat() {
        return matrixFloatBuffer.next();
    }

    /**
     * @return the next free ray.
     */
    @NotNull
    public Ray nextRay() {
        return rayBuffer.next();
    }

    /**
     * @return the next free rotation.
     */
    @NotNull
    public Quaternion nextRotation() {
        return rotationBuffer.next();
    }

    /**
     * @return the next free vector.
     */
    @NotNull
    public Vector3f nextVector() {
        return vectorBuffer.next();
    }

    /**
     * @return the next free vector.
     */
    @NotNull
    public Vector2f nextVector2f() {
        return vector2fBuffer.next();
    }

    /**
     * @return the next free plane.
     */
    @NotNull
    public Plane nextPlane() {
        return planeBuffer.next();
    }

    /**
     * @return the next color.
     */
    @NotNull
    public ColorRGBA nextColor() {
        return colorBuffer.next();
    }
}
