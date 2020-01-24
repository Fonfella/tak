package api;

public interface ServoController
{
    void set(String servoId, float position);
    void raw(String servoId, float position, short speed, short acceleration);
}
