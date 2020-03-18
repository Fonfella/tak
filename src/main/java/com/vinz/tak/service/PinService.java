package com.vinz.tak.service;

import com.vinz.tak.model.Pin;
import com.vinz.tak.model.ProcessOptions;
import com.vinz.tak.util.ProcessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

import static com.vinz.tak.service.TapeDeck.DEVICE_OPTION;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class PinService extends AbstractService {

    public static final int PIN_SEND_TIMEOUT = 10000;

    @Autowired
    private AdbExecutor adbExecutor;

    @Autowired
    private ProcessUtils processUtils;

    public Pin sendPin(Pin pin) {

        ProcessOptions options = new ProcessOptions();
        options.setTimeout(PIN_SEND_TIMEOUT);

        String hexPin = convertToHex(pin.getPin());

        String[] shell = {"shell"};

        String did = pin.getDid();

        if (!isEmpty(did)) {

            shell = processUtils.prepender(shell, DEVICE_OPTION, did);
        }

        String[] emvOn = {"am",
                "starservice",
                "-a",
                "action.km",
                "-e",
                "param.command",
                "generic",
                "-e",
                "param.id",
                "FF09",
                "-e",
                "param.data",
                "01",
                "-e",
                "param.sequence_num",
                "123"};

        String[] sendPin = {
                "am",
                "startservice",
                "-a",
                "action.km",
                "-e",
                "param.command",
                "generic",
                "-e",
                "param.sequence_num",
                "123",
                "-e",
                "param.id",
                "FF07",
                "-e",
                "param.data",
                hexPin
        };

        adbExecutor.adb(options, processUtils.prepender(emvOn, shell));

        adbExecutor.adb(options, processUtils.prepender(sendPin, shell));

        return pin;
    }

    private String convertToHex(String pin) {

        byte[] bytes = pin.getBytes(Charset.defaultCharset());

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {

            sb.append(Integer.toHexString(b));
        }

        return sb.toString();
    }

}
