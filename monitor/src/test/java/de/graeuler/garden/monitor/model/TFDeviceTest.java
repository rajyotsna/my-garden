package de.graeuler.garden.monitor.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.EnumMap;

import org.junit.Test;

import com.tinkerforge.BrickMaster;
import com.tinkerforge.BrickRED;

public class TFDeviceTest {
	
	private EnumMap<TFDevice.Version, Short> buildVersionMap(int major, int minor, int release) {
		EnumMap<TFDevice.Version, Short> version = new EnumMap<>(TFDevice.Version.class);
		version.put(TFDevice.Version.MAJOR,   Short.valueOf((short)major));
		version.put(TFDevice.Version.MINOR,   Short.valueOf((short)minor));
		version.put(TFDevice.Version.RELEASE, Short.valueOf((short)release));
		return version;
	}

	@Test
	public void testCreate() {
		TFDevice device = TFDevice.create("xyz", "abc", 'a', new short[] {2,0,1}, new short[] {1,2,3}, BrickRED.DEVICE_IDENTIFIER, (short) 2);
		assertEquals("xyz", device.getUid());
		assertEquals("abc", device.getConnectedTo());
		assertEquals('a', device.getPosition());
		assertEquals(buildVersionMap(2,0,1), device.getHwv());
		assertEquals(buildVersionMap(1,2,3), device.getFwv());
		assertEquals(BrickRED.class, device.deviceClass);
		assertEquals(TFDevice.State.DISCONNECTED, device.getState());
		// Try to break it and assert correct behavior
		device = TFDevice.create(null, null, '1', null, null, Integer.MAX_VALUE, (short) 3);
		assertNull(device); // invalid device identifier Integer.MAX_VALUE results in returning null.
		device = TFDevice.create(null, null, '1', null, null, BrickMaster.DEVICE_IDENTIFIER, (short) 3);
		assertNull(device.getUid());
		assertNull(device.getConnectedTo());
		assertEquals('1', device.getPosition());
		assertTrue(device.getHwv().isEmpty());
		assertTrue(device.getFwv().isEmpty());
		assertNull(device.getState());
	}

	@Test
	public void testClassIsA() {
		TFDevice device = TFDevice.create("xyz", "abc", 'a', new short[] {2,0,1}, new short[] {1,2,3}, BrickRED.DEVICE_IDENTIFIER, (short) 2);
		assertTrue(device.classIsA(BrickRED.class));
		assertFalse(device.classIsA(null));
	}

}
