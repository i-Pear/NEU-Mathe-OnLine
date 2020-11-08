package com.ipear.web.training.mapper;

import com.ipear.web.training.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User,Integer> {

    User getUserByUid(int uid);

}
