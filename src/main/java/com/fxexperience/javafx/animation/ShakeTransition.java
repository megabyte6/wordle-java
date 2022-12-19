package com.fxexperience.javafx.animation;

import static javafx.util.Duration.millis;
import static javafx.util.Duration.seconds;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a shake effect on the given node
 * 
 * Port of Shake from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes shake {
 * 	0%, 100% {transform: translateX(0);}
 * 	10%, 30%, 50%, 70%, 90% {transform: translateX(-10px);}
 * 	20%, 40%, 60%, 80% {transform: translateX(10px);}
 * }
 * 
 * @author Jasper Potts
 */
public class ShakeTransition extends CachedTimelineTransition {

    /**
     * Create new ShakeTransition
     * 
     * @param node The node to affect
     */
    public ShakeTransition(final Node node) {
        this(node, seconds(1), millis(200));
    }

    /**
     * Create new ShakeTransition
     * 
     * @param node The node to affect
     */
    public ShakeTransition(final Node node, Duration cycleDuration, Duration startDelay) {
        super(
                node,
                new Timeline(
                        new KeyFrame(millis(0), new KeyValue(node.translateXProperty(), 0, WEB_EASE)),
                        new KeyFrame(millis(100), new KeyValue(node.translateXProperty(), -10, WEB_EASE)),
                        new KeyFrame(millis(200), new KeyValue(node.translateXProperty(), 10, WEB_EASE)),
                        new KeyFrame(millis(300), new KeyValue(node.translateXProperty(), -10, WEB_EASE)),
                        new KeyFrame(millis(400), new KeyValue(node.translateXProperty(), 10, WEB_EASE)),
                        new KeyFrame(millis(500), new KeyValue(node.translateXProperty(), -10, WEB_EASE)),
                        new KeyFrame(millis(600), new KeyValue(node.translateXProperty(), 10, WEB_EASE)),
                        new KeyFrame(millis(700), new KeyValue(node.translateXProperty(), -10, WEB_EASE)),
                        new KeyFrame(millis(800), new KeyValue(node.translateXProperty(), 10, WEB_EASE)),
                        new KeyFrame(millis(900), new KeyValue(node.translateXProperty(), -10, WEB_EASE)),
                        new KeyFrame(millis(1000), new KeyValue(node.translateXProperty(), 0, WEB_EASE))));
        setCycleDuration(cycleDuration);
        setDelay(startDelay);
    }

}
