package hardware.pololu;

import hardware.servo.calibrate.DefaultingServoSourceCalibrator;
import hardware.servo.calibrate.ImmutableServoCalibrator;
import hardware.servo.calibrate.ServoCalibrator;
import hardware.util.math.InterRangeDoubleConverter;

import static hardware.util.math.InterRangeDoubleConverter.rangeFromNormalTo;

/**
 * A {@link DefaultingServoSourceCalibrator} that normalizes Pololu Maestro servos.
 *
 * @author Greg Elderfield
 */
public class DefaultPololuMaestroServoNormalization extends DefaultingServoSourceCalibrator<Integer>
{

    public DefaultPololuMaestroServoNormalization()
    {

        super(defaultCalibrationForEachServo());
    }

    private static ServoCalibrator defaultCalibrationForEachServo()
    {

        final InterRangeDoubleConverter valueNormalization = rangeFromNormalTo(4000.0, 8000.0);
        final InterRangeDoubleConverter speedNormalization = rangeFromNormalTo(0.0, 200.0);
        final InterRangeDoubleConverter accelerationNormalization = rangeFromNormalTo(0.0, 255.0);

        return new ImmutableServoCalibrator(valueNormalization, speedNormalization, accelerationNormalization);
    }
}
