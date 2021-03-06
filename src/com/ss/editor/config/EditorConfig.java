package com.ss.editor.config;

import static java.util.Objects.requireNonNull;
import static rlib.util.Utils.get;
import com.jme3.asset.AssetEventListener;
import com.jme3.asset.AssetKey;
import com.jme3.asset.TextureKey;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;
import com.ss.editor.Editor;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.util.EditorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * The user configuration of this editor.
 *
 * @author JavaSaBr
 */
public final class EditorConfig implements AssetEventListener {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(EditorConfig.class);

    private static final String GRAPHICS_ALIAS = "Graphics";
    private static final String SCREEN_ALIAS = "Screen";
    private static final String ASSET_ALIAS = "ASSET";
    private static final String ASSET_OTHER = "Other";
    private static final String ASSET_EDITING = "Editing";

    private static final String PREF_SCREEN_WIDTH = SCREEN_ALIAS + "." + "screenWidth";
    private static final String PREF_SCREEN_HEIGHT = SCREEN_ALIAS + "." + "screenHeight";
    private static final String PREF_SCREEN_MAXIMIZED = SCREEN_ALIAS + "." + "screenMaximized";
    private static final String PREF_SCREEN_DECORATED = SCREEN_ALIAS + "." + "decorated";

    private static final String PREF_GRAPHIC_ANISOTROPY = GRAPHICS_ALIAS + "." + "anisotropy";
    private static final String PREF_GRAPHIC_FRAME_RATE = GRAPHICS_ALIAS + "." + "frameRate";
    private static final String PREF_GRAPHIC_CAMERA_ANGLE = GRAPHICS_ALIAS + "." + "cameraAngle";
    private static final String PREF_GRAPHIC_FXAA = GRAPHICS_ALIAS + "." + "fxaa";
    private static final String PREF_GRAPHIC_GAMA_CORRECTION = GRAPHICS_ALIAS + "." + "gammaCorrection";
    private static final String PREF_GRAPHIC_TONEMAP_FILTER = GRAPHICS_ALIAS + "." + "toneMapFilter";
    private static final String PREF_GRAPHIC_TONEMAP_FILTER_WHITE_POINT = GRAPHICS_ALIAS + "." + "toneMapFilterWhitePoint";

    private static final String PREF_CURRENT_ASSET = ASSET_ALIAS + "." + "currentAsset";
    private static final String PREF_LAST_OPENED_ASSETS = ASSET_ALIAS + "." + "lastOpenedAssets";

    private static final String PREF_ADDITIONAL_CLASSPATH = ASSET_OTHER + "." + "additionalClasspath";
    private static final String PREF_ADDITIONAL_ENVS = ASSET_OTHER + "." + "additionalEnvs";
    private static final String PREF_GLOBAL_LEFT_TOOL_WIDTH = ASSET_OTHER + "." + "globalLeftToolWidth";
    private static final String PREF_GLOBAL_LEFT_TOOL_COLLAPSED = ASSET_OTHER + "." + "globalLeftToolCollapsed";
    private static final String PREF_GLOBAL_BOTTOM_TOOL_WIDTH = ASSET_OTHER + "." + "globalBottomToolWidth";
    private static final String PREF_GLOBAL_BOTTOM_TOOL_COLLAPSED = ASSET_OTHER + "." + "globalBottomToolCollapsed";
    private static final String PREF_ANALYTICS = ASSET_OTHER + "." + "analytics";
    private static final String PREF_AUTO_TANGENT_GENERATING = ASSET_EDITING + "." + "autoTangentGenerating";
    private static final String PREF_DEFAULT_USE_FLIPPED_TEXTURE = ASSET_EDITING + "." + "defaultUseFlippedTexture";
    private static final String PREF_CAMERA_LAMP_ENABLED = ASSET_EDITING + "." + "defaultCameraLampEnabled";
    private static final String PREF_ANALYTICS_QUESTION = ASSET_OTHER + "." + "analyticsQuestion" + Config.VERSION;

    @Nullable
    private static volatile EditorConfig instance;

    @NotNull
    public static EditorConfig getInstance() {

        if (instance == null) {

            final EditorConfig config = new EditorConfig();
            config.init();

            instance = config;
        }

        return instance;
    }

    /**
     * The list of last opened asset folders.
     */
    @NotNull
    private final List<String> lastOpenedAssets;

    /**
     * The current white point for the tone map filter.
     */
    @Nullable
    private volatile Vector3f toneMapFilterWhitePoint;

    /**
     * The current asset folder.
     */
    @Nullable
    private volatile Path currentAsset;

    /**
     * The path to the folder with additional classpath.
     */
    @Nullable
    private volatile Path additionalClasspath;

    /**
     * The path to the folder with additional envs.
     */
    @Nullable
    private volatile Path additionalEnvs;

    /**
     * The current level of the anisotropy.
     */
    private volatile int anisotropy;

    /**
     * The current frame rate.
     */
    private volatile int frameRate;

    /**
     * The current camera angle.
     */
    private volatile int cameraAngle;

    /**
     * The width of this screen.
     */
    private volatile int screenWidth;

    /**
     * The height of this screen.
     */
    private volatile int screenHeight;

    /**
     * The global left tool width.
     */
    private volatile int globalLeftToolWidth;

    /**
     * The global bottom tool width.
     */
    private volatile int globalBottomToolWidth;

    /**
     * Flag is for collapsing the global left tool.
     */
    private volatile boolean globalLeftToolCollapsed;

    /**
     * Flag is for collapsing the global bottom tool.
     */
    private volatile boolean globalBottomToolCollapsed;

    /**
     * Flag is for enabling the FXAA.
     */
    private volatile boolean fxaa;

    /**
     * Flag is for enabling the gamma correction.
     */
    private volatile boolean gammaCorrection;

    /**
     * Flag is for enabling the tone map filter.
     */
    private volatile boolean toneMapFilter;

    /**
     * Flag is for maximizing a window.
     */
    private volatile boolean maximized;

    /**
     * Flag is for decorating a window.
     */
    private volatile boolean decorated;

    /**
     * Flag is of enabling analytics.
     */
    private volatile boolean analytics;

    /**
     * Flag is of enabling auto tangent generating.
     */
    private volatile boolean autoTangentGenerating;

    /**
     * Flag is of enabling using flip textures by default.
     */
    private volatile boolean defaultUseFlippedTexture;

    /**
     * Flag is of enabling camera lamp in editors by default.
     */
    private volatile boolean defaultEditorCameraEnabled;

    /**
     * Flag is of showing analytics question.
     */
    private volatile boolean analyticsQuestion;

    public EditorConfig() {
        this.lastOpenedAssets = new ArrayList<>();
    }

    /**
     * @return The list of last opened asset folders.
     */
    @NotNull
    @FromAnyThread
    public synchronized List<String> getLastOpenedAssets() {
        return lastOpenedAssets;
    }

    /**
     * Add the new last opened asset folder.
     */
    @FromAnyThread
    public synchronized void addOpenedAsset(@NotNull final Path currentAsset) {

        final String filePath = currentAsset.toString();

        final List<String> lastOpenedAssets = getLastOpenedAssets();
        lastOpenedAssets.remove(filePath);
        lastOpenedAssets.add(0, filePath);

        if (lastOpenedAssets.size() > 10) lastOpenedAssets.remove(lastOpenedAssets.size() - 1);
    }

    @Override
    public void assetDependencyNotFound(@Nullable final AssetKey parentKey, @Nullable final AssetKey dependentAssetKey) {
    }

    @Override
    public void assetLoaded(@NotNull final AssetKey key) {
    }

    @Override
    public void assetRequested(@NotNull final AssetKey key) {
        if (key instanceof TextureKey) {
            ((TextureKey) key).setAnisotropy(getAnisotropy());
        }
    }

    /**
     * @param anisotropy the new level of the anisotropy.
     */
    @FromAnyThread
    public void setAnisotropy(final int anisotropy) {
        this.anisotropy = anisotropy;
    }

    /**
     * @return the current level of the anisotropy.
     */
    @FromAnyThread
    public int getAnisotropy() {
        return anisotropy;
    }

    /**
     * @param fxaa flag is for enabling the FXAA.
     */
    @FromAnyThread
    public void setFXAA(final boolean fxaa) {
        this.fxaa = fxaa;
    }

    /**
     * @return flag is for enabling the FXAA.
     */
    @FromAnyThread
    public boolean isFXAA() {
        return fxaa;
    }

    /**
     * @return the current asset folder.
     */
    @Nullable
    @FromAnyThread
    public Path getCurrentAsset() {
        return currentAsset;
    }

    /**
     * @param currentAsset the new current asset folder.
     */
    @FromAnyThread
    public void setCurrentAsset(@Nullable final Path currentAsset) {
        this.currentAsset = currentAsset;
    }

    /**
     * @return путь к папке с дополнительным classpath.
     */
    @Nullable
    @FromAnyThread
    public Path getAdditionalClasspath() {
        return additionalClasspath;
    }

    /**
     * @param additionalClasspath путь к папке с дополнительным classpath.
     */
    @FromAnyThread
    public void setAdditionalClasspath(@Nullable final Path additionalClasspath) {
        this.additionalClasspath = additionalClasspath;
    }

    /**
     * @return the path to the folder with additional envs.
     */
    @Nullable
    @FromAnyThread
    public Path getAdditionalEnvs() {
        return additionalEnvs;
    }

    /**
     * @param additionalEnvs the path to the folder with additional envs.
     */
    @FromAnyThread
    public void setAdditionalEnvs(@Nullable final Path additionalEnvs) {
        this.additionalEnvs = additionalEnvs;
    }

    /**
     * @return flag is for enabling the gamma correction.
     */
    @FromAnyThread
    public boolean isGammaCorrection() {
        return gammaCorrection;
    }

    /**
     * @param gammaCorrection flag is for enabling the gamma correction.
     */
    @FromAnyThread
    public void setGammaCorrection(final boolean gammaCorrection) {
        this.gammaCorrection = gammaCorrection;
    }

    /**
     * @return flag is for enabling the tone map filter.
     */
    @FromAnyThread
    public boolean isToneMapFilter() {
        return toneMapFilter;
    }

    /**
     * @param toneMapFilter flag is for enabling the tone map filter.
     */
    @FromAnyThread
    public void setToneMapFilter(final boolean toneMapFilter) {
        this.toneMapFilter = toneMapFilter;
    }

    /**
     * @return the current white point for the tone map filter.
     */
    @NotNull
    @FromAnyThread
    public Vector3f getToneMapFilterWhitePoint() {
        return requireNonNull(toneMapFilterWhitePoint);
    }

    /**
     * @param toneMapFilterWhitePoint the new white point for the tone map filter.
     */
    @FromAnyThread
    public void setToneMapFilterWhitePoint(@NotNull final Vector3f toneMapFilterWhitePoint) {
        this.toneMapFilterWhitePoint = toneMapFilterWhitePoint;
    }

    /**
     * @param screenHeight the height of this screen.
     */
    @FromAnyThread
    public void setScreenHeight(final int screenHeight) {
        this.screenHeight = screenHeight;
    }

    /**
     * @param screenWidth the width of this screen.
     */
    @FromAnyThread
    public void setScreenWidth(final int screenWidth) {
        this.screenWidth = screenWidth;
    }

    /**
     * @return the height of this screen.
     */
    @FromAnyThread
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * @return the width of this screen.
     */
    @FromAnyThread
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * @return true is a window is maximized.
     */
    @FromAnyThread
    public boolean isMaximized() {
        return maximized;
    }

    /**
     * @param maximized flag is for maximizing a window.
     */
    @FromAnyThread
    public void setMaximized(final boolean maximized) {
        this.maximized = maximized;
    }

    /**
     * @return true if this windows needs to decorate.
     */
    @FromAnyThread
    public boolean isDecorated() {
        return decorated;
    }

    /**
     * @param decorated flag is for decorating a window.
     */
    @FromAnyThread
    public void setDecorated(final boolean decorated) {
        this.decorated = decorated;
    }

    /**
     * @return the global left tool width.
     */
    @FromAnyThread
    public int getGlobalLeftToolWidth() {
        return globalLeftToolWidth;
    }

    /**
     * @return the global bottom tool width.
     */
    @FromAnyThread
    public int getGlobalBottomToolWidth() {
        return globalBottomToolWidth;
    }

    /**
     * @param globalLeftToolWidth the global left tool width.
     */
    @FromAnyThread
    public void setGlobalLeftToolWidth(final int globalLeftToolWidth) {
        this.globalLeftToolWidth = globalLeftToolWidth;
    }

    /**
     * @param globalBottomToolWidth the global bottom tool width.
     */
    @FromAnyThread
    public void setGlobalBottomToolWidth(final int globalBottomToolWidth) {
        this.globalBottomToolWidth = globalBottomToolWidth;
    }

    /**
     * @param globalLeftToolCollapsed flag is for collapsing the global left tool.
     */
    @FromAnyThread
    public void setGlobalLeftToolCollapsed(final boolean globalLeftToolCollapsed) {
        this.globalLeftToolCollapsed = globalLeftToolCollapsed;
    }

    /**
     * @param globalBottomToolCollapsed flag is for collapsing the global bottom tool.
     */
    @FromAnyThread
    public void setGlobalBottomToolCollapsed(final boolean globalBottomToolCollapsed) {
        this.globalBottomToolCollapsed = globalBottomToolCollapsed;
    }

    /**
     * @return true if the global left tool is collapsed.
     */
    @FromAnyThread
    public boolean isGlobalLeftToolCollapsed() {
        return globalLeftToolCollapsed;
    }

    /**
     * @return true if the global bottom tool is collapsed.
     */
    @FromAnyThread
    public boolean isGlobalBottomToolCollapsed() {
        return globalBottomToolCollapsed;
    }

    /**
     * @param analytics true if you want to enable analytics.
     */
    @FromAnyThread
    public void setAnalytics(final boolean analytics) {
        this.analytics = analytics;
    }

    /**
     * @return true if analytics is enabled.
     */
    @FromAnyThread
    public boolean isAnalytics() {
        return analytics;
    }

    /**
     * @return true if enabled auto tangent generating.
     */
    @FromAnyThread
    public boolean isAutoTangentGenerating() {
        return autoTangentGenerating;
    }

    /**
     * @param autoTangentGenerating flag is of enabling auto tangent generating.
     */
    @FromAnyThread
    public void setAutoTangentGenerating(final boolean autoTangentGenerating) {
        this.autoTangentGenerating = autoTangentGenerating;
    }

    /**
     * @return true if use flip textures by default.
     */
    public boolean isDefaultUseFlippedTexture() {
        return defaultUseFlippedTexture;
    }

    /**
     * @param defaultUseFlippedTexture flag is of enabling using flip textures by default.
     */
    public void setDefaultUseFlippedTexture(final boolean defaultUseFlippedTexture) {
        this.defaultUseFlippedTexture = defaultUseFlippedTexture;
    }

    /**
     * @return true if enable camera lamp by default.
     */
    public boolean isDefaultEditorCameraEnabled() {
        return defaultEditorCameraEnabled;
    }

    /**
     * @param defaultEditorCameraEnabled Flag is of enabling camera lamp in editors by default.
     */
    public void setDefaultEditorCameraEnabled(final boolean defaultEditorCameraEnabled) {
        this.defaultEditorCameraEnabled = defaultEditorCameraEnabled;
    }

    /**
     * @return the current frameRate.
     */
    @FromAnyThread
    public int getFrameRate() {
        return frameRate;
    }

    /**
     * @param frameRate the current frameRate.
     */
    @FromAnyThread
    public void setFrameRate(final int frameRate) {
        this.frameRate = frameRate;
    }

    /**
     * @param cameraAngle the camera angle.
     */
    @FromAnyThread
    public void setCameraAngle(final int cameraAngle) {
        this.cameraAngle = cameraAngle;
    }

    /**
     * @return the camera angle.
     */
    @FromAnyThread
    public int getCameraAngle() {
        return cameraAngle;
    }

    /**
     * @return true if the question was showed.
     */
    public boolean isAnalyticsQuestion() {
        return analyticsQuestion;
    }

    /**
     * @param analyticsQuestion true if the question was showed.
     */
    public void setAnalyticsQuestion(final boolean analyticsQuestion) {
        this.analyticsQuestion = analyticsQuestion;
    }

    /**
     * @return the settings for JME.
     */
    @FromAnyThread
    public AppSettings getSettings() {

        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice device = graphicsEnvironment.getDefaultScreenDevice();
        final DisplayMode displayMode = device.getDisplayMode();

        final AppSettings settings = new AppSettings(true);
        settings.setFrequency(displayMode.getRefreshRate());
        settings.setGammaCorrection(isGammaCorrection());
        settings.setResizable(true);
        // settings.putBoolean("GraphicsDebug", true);

        try {

            final BufferedImage[] icons = new BufferedImage[5];
            icons[0] = ImageIO.read(EditorUtil.getInputStream("/ui/icons/app/SSEd256.png"));
            icons[1] = ImageIO.read(EditorUtil.getInputStream("/ui/icons/app/SSEd128.png"));
            icons[2] = ImageIO.read(EditorUtil.getInputStream("/ui/icons/app/SSEd64.png"));
            icons[3] = ImageIO.read(EditorUtil.getInputStream("/ui/icons/app/SSEd32.png"));
            icons[4] = ImageIO.read(EditorUtil.getInputStream("/ui/icons/app/SSEd16.png"));

            settings.setIcons(icons);

        } catch (final IOException e) {
            LOGGER.warning(e);
        }

        JmeToJFXIntegrator.prepareSettings(settings, getFrameRate());

        return settings;
    }

    /**
     * Load user settings.
     */
    private void init() {

        final Preferences prefs = Preferences.userNodeForPackage(Editor.class);

        this.anisotropy = prefs.getInt(PREF_GRAPHIC_ANISOTROPY, 0);
        this.fxaa = prefs.getBoolean(PREF_GRAPHIC_FXAA, false);
        this.gammaCorrection = prefs.getBoolean(PREF_GRAPHIC_GAMA_CORRECTION, false);
        this.toneMapFilter = prefs.getBoolean(PREF_GRAPHIC_TONEMAP_FILTER, false);
        this.maximized = prefs.getBoolean(PREF_SCREEN_MAXIMIZED, false);
        this.screenHeight = prefs.getInt(PREF_SCREEN_HEIGHT, 800);
        this.screenWidth = prefs.getInt(PREF_SCREEN_WIDTH, 1200);
        this.globalLeftToolWidth = prefs.getInt(PREF_GLOBAL_LEFT_TOOL_WIDTH, 300);
        this.globalLeftToolCollapsed = prefs.getBoolean(PREF_GLOBAL_LEFT_TOOL_COLLAPSED, false);
        this.globalBottomToolWidth = prefs.getInt(PREF_GLOBAL_BOTTOM_TOOL_WIDTH, 300);
        this.globalBottomToolCollapsed = prefs.getBoolean(PREF_GLOBAL_BOTTOM_TOOL_COLLAPSED, true);
        this.decorated = prefs.getBoolean(PREF_SCREEN_DECORATED, true);
        this.analytics = prefs.getBoolean(PREF_ANALYTICS, true);
        this.frameRate = prefs.getInt(PREF_GRAPHIC_FRAME_RATE, 40);
        this.cameraAngle = prefs.getInt(PREF_GRAPHIC_CAMERA_ANGLE, 45);
        this.autoTangentGenerating = prefs.getBoolean(PREF_AUTO_TANGENT_GENERATING, false);
        this.defaultUseFlippedTexture = prefs.getBoolean(PREF_DEFAULT_USE_FLIPPED_TEXTURE, true);
        this.defaultEditorCameraEnabled = prefs.getBoolean(PREF_CAMERA_LAMP_ENABLED, true);
        this.analyticsQuestion = prefs.getBoolean(PREF_ANALYTICS_QUESTION, false);

        final String currentAssetURI = prefs.get(PREF_CURRENT_ASSET, null);

        if (currentAssetURI != null) {
            this.currentAsset = get(currentAssetURI, uri -> Paths.get(new URI(uri)));
        }

        final String classpathURI = prefs.get(PREF_ADDITIONAL_CLASSPATH, null);

        if (classpathURI != null) {
            this.additionalClasspath = get(classpathURI, uri -> Paths.get(new URI(uri)));
        }

        final String envsURI = prefs.get(PREF_ADDITIONAL_ENVS, null);

        if (envsURI != null) {
            this.additionalEnvs = get(envsURI, uri -> Paths.get(new URI(uri)));
        }

        this.toneMapFilterWhitePoint = new Vector3f(11, 11, 11);

        final String whitePoint = prefs.get(PREF_GRAPHIC_TONEMAP_FILTER_WHITE_POINT, null);
        final String[] coords = whitePoint == null ? null : whitePoint.split(",", 3);

        if (coords != null && coords.length > 2) {
            try {
                toneMapFilterWhitePoint.setX(Float.parseFloat(coords[0]));
                toneMapFilterWhitePoint.setY(Float.parseFloat(coords[1]));
                toneMapFilterWhitePoint.setZ(Float.parseFloat(coords[2]));
            } catch (NumberFormatException e) {
                LOGGER.error(e);
            }
        }

        final byte[] byteArray = prefs.getByteArray(PREF_LAST_OPENED_ASSETS, null);
        if (byteArray == null) return;

        final List<String> lastOpenedAssets = getLastOpenedAssets();
        try {
            lastOpenedAssets.addAll(EditorUtil.deserialize(byteArray));
        } catch (final RuntimeException e) {
            LOGGER.warning(e);
        }

        System.setProperty("jfx.frame.transfer.camera.angle", String.valueOf(getCameraAngle()));
    }

    /**
     * Save these settings.
     */
    @FromAnyThread
    public synchronized void save() {

        final Preferences prefs = Preferences.userNodeForPackage(Editor.class);
        prefs.putInt(PREF_GRAPHIC_ANISOTROPY, getAnisotropy());
        prefs.putBoolean(PREF_GRAPHIC_FXAA, isFXAA());
        prefs.putBoolean(PREF_GRAPHIC_GAMA_CORRECTION, isGammaCorrection());
        prefs.putBoolean(PREF_GRAPHIC_TONEMAP_FILTER, isToneMapFilter());
        prefs.putInt(PREF_SCREEN_HEIGHT, getScreenHeight());
        prefs.putInt(PREF_SCREEN_WIDTH, getScreenWidth());
        prefs.putBoolean(PREF_SCREEN_MAXIMIZED, isMaximized());
        prefs.putInt(PREF_GLOBAL_LEFT_TOOL_WIDTH, getGlobalLeftToolWidth());
        prefs.putBoolean(PREF_GLOBAL_LEFT_TOOL_COLLAPSED, isGlobalLeftToolCollapsed());
        prefs.putInt(PREF_GLOBAL_BOTTOM_TOOL_WIDTH, getGlobalBottomToolWidth());
        prefs.putBoolean(PREF_GLOBAL_BOTTOM_TOOL_COLLAPSED, isGlobalBottomToolCollapsed());
        prefs.putBoolean(PREF_SCREEN_DECORATED, isDecorated());
        prefs.putBoolean(PREF_ANALYTICS, isAnalytics());
        prefs.putInt(PREF_GRAPHIC_FRAME_RATE, getFrameRate());
        prefs.putInt(PREF_GRAPHIC_CAMERA_ANGLE, getCameraAngle());
        prefs.putBoolean(PREF_AUTO_TANGENT_GENERATING, isAutoTangentGenerating());
        prefs.putBoolean(PREF_DEFAULT_USE_FLIPPED_TEXTURE, isDefaultUseFlippedTexture());
        prefs.putBoolean(PREF_CAMERA_LAMP_ENABLED, isDefaultEditorCameraEnabled());
        prefs.putBoolean(PREF_ANALYTICS_QUESTION, isAnalyticsQuestion());

        final Vector3f whitePoint = getToneMapFilterWhitePoint();

        prefs.put(PREF_GRAPHIC_TONEMAP_FILTER_WHITE_POINT, whitePoint.getX() + "," + whitePoint.getY() + "," + whitePoint.getZ());

        if (currentAsset != null && !Files.exists(currentAsset)) {
            currentAsset = null;
        }

        if (additionalClasspath != null && !Files.exists(additionalClasspath)) {
            additionalClasspath = null;
        }

        if (currentAsset != null) {
            prefs.put(PREF_CURRENT_ASSET, currentAsset.toUri().toString());
        } else {
            prefs.remove(PREF_CURRENT_ASSET);
        }

        if (additionalClasspath != null) {
            prefs.put(PREF_ADDITIONAL_CLASSPATH, additionalClasspath.toUri().toString());
        } else {
            prefs.remove(PREF_ADDITIONAL_CLASSPATH);
        }

        if (additionalEnvs != null) {
            prefs.put(PREF_ADDITIONAL_ENVS, additionalEnvs.toUri().toString());
        } else {
            prefs.remove(PREF_ADDITIONAL_ENVS);
        }

        final List<String> lastOpenedAssets = getLastOpenedAssets();

        prefs.putByteArray(PREF_LAST_OPENED_ASSETS, EditorUtil.serialize((Serializable) lastOpenedAssets));
        try {
            prefs.flush();
        } catch (final BackingStoreException e) {
            throw new RuntimeException(e);
        }

        System.setProperty("jfx.frame.transfer.camera.angle", String.valueOf(getCameraAngle()));
    }
}
