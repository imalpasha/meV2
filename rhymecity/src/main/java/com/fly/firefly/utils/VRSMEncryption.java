package com.fly.firefly.utils;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class VRSMEncryption
{
	public static String getEncryptedData(String str, String password)
	{
		if (str == null || "".equals(str))
		{
			return "";
		}
		int iterationCount = 10;
		byte[] salt = {(byte) 0xB2, (byte) 0x12, (byte) 0xD5, (byte) 0xB2, (byte) 0x44, (byte) 0x21, (byte) 0xC3, (byte) 0xC3};
		Cipher ecipher;
		try
		{
			KeySpec keySpec = new PBEKeySpec(str.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			ecipher = Cipher.getInstance("PBEWithMD5AndDES");

			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			byte[] utf8 = password.getBytes("UTF8");
			byte[] enc = ecipher.doFinal(utf8);
			enc = Base64.encode(enc, Base64.DEFAULT);
			return new String(enc);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "";
	}

	public static String getDecryptedData(String str, String password)
	{
		if (str == null || "".equals(str))
		{
			return "";
		}
		int iterationCount = 10;
		byte[] salt = {(byte) 0xB2, (byte) 0x12, (byte) 0xD5, (byte) 0xB2, (byte) 0x44, (byte) 0x21, (byte) 0xC3, (byte) 0xC3};
		Cipher dcipher;

		try
		{
			KeySpec keySpec = new PBEKeySpec(str.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			dcipher = Cipher.getInstance("PBEWithMD5AndDES");

			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			byte[] dec = Base64.decode(password.getBytes(), Base64.DEFAULT);
			byte[] utf8 = dcipher.doFinal(dec);
			return new String(utf8, "UTF8");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "";
	}

	public static String getSHA256(String value) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(value.getBytes());

		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0 ; i < byteData.length ; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}

		Log.d("VRSMEncryption", "Hex format : " + sb.toString());

		return sb.toString();
	}
}
