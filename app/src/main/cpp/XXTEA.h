//
// Created by lijie on 2017/7/6.
//

#include <string>
#include <jni.h>
#include <malloc.h>


#define XXTEA_MX (z >> 5 ^ y << 2) + (y >> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z)
#define XXTEA_DELTA 0x9E3779B9

std::string xxtea_encrypt(const std::string data, const std::string key);

std::string xxtea_decrypt(const std::string data, const std::string key);

