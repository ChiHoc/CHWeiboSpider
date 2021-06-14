package com.chiho.server.security

import org.apache.tomcat.util.codec.binary.Base64
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import kotlin.experimental.and

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 9:19 下午
 */

object CryptUtils {
    private const val DES = "DES"

    /**
     * 加密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     *
     * @return 返回加密后的数据
     *
     * @throws Exception
     */
    fun encrypt(src: ByteArray?, key: ByteArray?): ByteArray {
        // DES算法要求有一个可信任的随机数源
        return try {
            val sr = SecureRandom()
            // 从原始密匙数据创建DESKeySpec对象
            val dks = DESKeySpec(key)
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            val keyFactory = SecretKeyFactory.getInstance(DES)
            val securekey = keyFactory.generateSecret(dks)
            // Cipher对象实际完成加密操作
            val cipher = Cipher.getInstance(DES)
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr)
            // 现在，获取数据并加密
            // 正式执行加密操作
            cipher.doFinal(src)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * 解密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     *
     * @return 返回解密后的原始数据
     *
     * @throws Exception
     */
    @Throws(RuntimeException::class)
    fun decrypt(src: ByteArray?, key: ByteArray?): ByteArray {
        return try {
            // DES算法要求有一个可信任的随机数源
            val sr = SecureRandom()
            // 从原始密匙数据创建一个DESKeySpec对象
            val dks = DESKeySpec(key)
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            val keyFactory = SecretKeyFactory.getInstance(DES)
            val securekey = keyFactory.generateSecret(dks)
            // Cipher对象实际完成解密操作
            val cipher = Cipher.getInstance(DES)
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr)
            // 现在，获取数据并解密
            // 正式执行解密操作
            cipher.doFinal(src)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * 数据解密
     *
     * @param key 密钥
     *
     * @return
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decrypt(data: String, key: String): String {
        return String(decrypt(hex2byte(data.toByteArray()), key.toByteArray()))
    }

    /**
     * 数据加密
     *
     * @param key 密钥
     *
     * @return
     *
     * @throws Exception
     */
    fun encrypt(data: String?, key: String): String? {
        return if (data != null) {
            try {
                byte2hex(encrypt(data.toByteArray(), key.toByteArray()))
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        } else null
    }

    fun decryptAndBase64(value: String?, key: String): String {
        return try {
            if (value == null) {
                ""
            } else String(decrypt(Base64.decodeBase64(value), key.toByteArray()),
                StandardCharsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun encryptAndBase64(value: String, key: String): String {
        return Base64.encodeBase64URLSafeString(encrypt(value.toByteArray(), key.toByteArray()))
    }

    /**
     * 二行制转字符串
     *
     * @return
     */
    private fun byte2hex(b: ByteArray?): String {
        val hs = StringBuilder()
        var stmp: String
        var n = 0
        while (b != null && n < b.size) {
            stmp = Integer.toHexString(b[n].toInt() and 0XFF)
            if (stmp.length == 1) {
                hs.append('0')
            }
            hs.append(stmp)
            n++
        }
        return hs.toString().uppercase(Locale.getDefault())
    }

    private fun hex2byte(b: ByteArray): ByteArray {
        require(b.size % 2 == 0)
        val b2 = ByteArray(b.size / 2)
        var n = 0
        while (n < b.size) {
            val item = String(b, n, 2)
            b2[n / 2] = item.toInt(16).toByte()
            n += 2
        }
        return b2
    }
}