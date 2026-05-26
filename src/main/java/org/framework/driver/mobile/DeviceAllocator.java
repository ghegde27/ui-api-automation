package org.framework.driver.mobile;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DeviceAllocator {

    private static final Set<String> LOCKED_DEVICES =
            ConcurrentHashMap.newKeySet();

    private DeviceAllocator() {}

    public static synchronized String allocate() {

        Set<String> connected = AdbCommands.getConnectedDevices();

        for (String device : connected) {
            if (LOCKED_DEVICES.add(device)) {
                return device;
            }
        }
        throw new RuntimeException("No free devices available");
    }

    public static void release(String device) {
        LOCKED_DEVICES.remove(device);
    }
}