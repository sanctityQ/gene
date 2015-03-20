package org.one.gene.domain.service.account;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.one.gene.domain.entity.User;
import org.one.gene.repository.UserRepository;
import org.one.gene.utils.Digests;
import org.one.gene.utils.Encodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AccountService {

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    public static final String HASH_ALGORITHM = "SHA-1";

    public static final int HASH_INTERATIONS = 1024;

    private static final int SALT_SIZE = 8;

    @Autowired
    private UserRepository userRepository;


    public User findUserByLoginName(String username) {
        return userRepository.findByCode(username);
    }

    public void registerUser(User user) {
        user.setCreateTime(DateTime.now().toDate());
        // 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
        if (StringUtils.isNotBlank(user.getPlainPassword())) {
            encryptPassword(user);
        }
        userRepository.save(user);
    }

    public void updateUser(User user){
        User oldUser = userRepository.findByCode(user.getCode());
        user.setPassword(oldUser.getPassword());
        userRepository.save(user);
    }


    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void encryptPassword(User user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));
        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }

}
