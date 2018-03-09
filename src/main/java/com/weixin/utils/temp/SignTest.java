package com.weixin.utils.temp;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class SignTest {
	private static final String SignALGORITHM = "MD5WithRSA";//算法

	protected static Logger log = LogManager.getLogger(SignTest.class);

	public static String sign(byte[] data, PrivateKey priKey) {
		try {
			Signature signature = Signature.getInstance(SignALGORITHM);
			signature.initSign(priKey);
			signature.update(data);
			return Base64.encodeBase64URLSafeString(signature.sign());
		} catch (Exception e) {
			log.error("sign message error", e);
			log.error("加密报文出错", e);
			return null;
		}
	}

	public static boolean verify(byte[] data, String sign, PublicKey pubKey) {
		try {
			Signature signature = Signature.getInstance(SignALGORITHM);
			signature.initVerify(pubKey);
			signature.update(data);
			return signature.verify(Base64.decodeBase64(sign));
		} catch (Exception e) {
			log.error("verify message error", e);
			log.error("解密报文出错", e);
			return false;
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		//私钥
		String cpicPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKiiYrrilaFHmK35O3pPQk3EX01Q5UraiBIlzAjct/XyJTwJuj+n3oXLSMjXnpiqVJE2TISUrRFDH32a7T390zwtY2w/cSwARiAOFP6JuLHDBOPOasaamK31/b4SgbiIXebInMQkOdnxkQv/dD5UhMnc7qcaqGfhlka8N6KdZ/L1AgMBAAECgYEAilKww3TqoaE1xraiKABtdZa3SZcqjYzVgnVIeJh7uR0Hl8iu4loTOBH4QO5EpBtLRNWSTEob8AXsBb6YDXpv0h7MPIatErPScdQRRa9+gMM8xEOPDvuJymb2dTvh9Y3zMKa4NFqSrnitU5fiWin95CrrpkyBJ8U+9y5D3PSiJL0CQQDTQ7o3M3BlzGzn2EwDrPMXB2OXScRa47WnRaSfF5hSGdt8m0mNUTf8paTH5tmeHD1z/NOl3recKWJJzksfq4rrAkEAzFe/2QCYIqFBjNP7sNLUtSenN0oQzdDgZdcTjvQJRK1mR1ILf6ZtwcF4kvjSrwbaDtrRpB3s32bhZx2LxwVBnwJAUVCrNx4EcBpL/LzjHTMPadi9O3j4K+ejfpuurlF9nnQ3Z/G5ULxnKUZ0c0DmmBXUjXVN3XUpBOFk5HWTsr/rOwJAeTCBo11q3P7uNWtzlkczx8ygq3XNyYLb0wpfmyS4ana8dgf+sdIuvf7UV7YinZNet/im5MKqvUPy4ojvYTWI2QJATX5qpa4AJItlJjfzbERLKVXibO7Bz19G5e1ewNH66g4Q31OYQrRKXxubjljRdNLkgNdIHmpswdW6FZ9d72P+JA==";
		PrivateKey privateKey = KeyPairer.getPrivateKey(cpicPrivateKey);
		//公钥
		String partnerPublicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoomK64pWhR5it+Tt6T0JNxF9NUOVK2ogSJcwI3Lf18iU8Cbo/p96Fy0jI156YqlSRNkyElK0RQx99mu09/dM8LWNsP3EsAEYgDhT+ibixwwTjzmrGmpit9f2+EoG4iF3myJzEJDnZ8ZEL/3Q+VITJ3O6nGqhn4ZZGvDeinWfy9QIDAQAB";
		PublicKey publicKey = KeyPairer.getPublicKey(partnerPublicKey);
		//签名数据
		String message = "<Request><InputsList><Inputs type=\"vehicleInfo\"><Input name=\"licenseNo\">苏B219PT</Input><Input name=\"cityCode\">320500</Input><Input name=\"noLicenseFlag\">0</Input><Input name=\"idNo\">5021</Input><Input name=\"idNoType\">09</Input><Input name=\"otherSource\">646</Input></Inputs></InputsList></Request>";//报文
		System.out.println("被加密报文:"+message);

		//生成签名
		String miwen=sign(message.getBytes("GBK"), privateKey);
		System.out.println("签名为:"+miwen);

		//验签
		boolean b=verify(message.getBytes("GBK"), miwen, publicKey);
		System.out.println("验签结果为:"+b);
	}
}