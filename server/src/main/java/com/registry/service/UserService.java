package com.registry.service;

import com.registry.repository.common.CodeEntity;
import com.registry.repository.common.CodeRepository;
import com.registry.repository.user.Role;
import com.registry.repository.user.RoleRepository;
import com.registry.repository.user.User;
import com.registry.repository.user.UserRepository;
import com.registry.util.SecurityUtil;
import com.registry.value.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by boozer on 2019. 6. 18
 */
@Service
@Transactional
public class UserService extends AbstractService {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Private Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /** 유저 Repo */
    @Autowired
    private UserRepository userRepo;

    /** 유저 롤 Repo */
    @Autowired
    private RoleRepository roleRepo;

    /** 공통 코드 Repo */
    @Autowired
    private CodeRepository codeRepo;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Protected Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Public Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Constructor
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Getter & Setter Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Public Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * 로그인 사용자정보를 반환한다.
     * @return
     * @throws Exception
     */
    public User getLoginUser() throws Exception {

        // 반환결과
        Result result = new Result();

        // 사용자정보 조회
        User user = userRepo.findById(SecurityUtil.getUser()).orElse(null);

        return user;
    }


    /**
     * 사용자정보를 조회한다.
     * @param username 유저 ID
     * @return
     * @throws Exception
     */
    public User getUserInfo(String username) {

        // 사용자정보 조회
        User user = userRepo.findUserByUsername(username);

        return user;
    }

    public List<User> getUsers() throws Exception {
        return userRepo.findAll();
    }

    public Map<String,Object> getUsers(String keyword, String role, int page, int size, String sortProperty, String sortDirection) throws Exception {
        List<User> totalList = userRepo.findAll();

        List<User> filteredList = new ArrayList<User>();
        int startIdx = (page - 1)   * size;
        int endIdx = startIdx + size;
        for(User user : totalList){
            boolean rolePass = false;
            boolean keywordPass = false;
            // 검색어 필터
            if(keyword.equals("")){
                keywordPass = true;
            }
            if(!keywordPass && (user.getEmail().indexOf(keyword) != -1 || user.getName().indexOf(keyword) != -1 || user.getUsername().indexOf(keyword) != -1)){
                keywordPass = true;
            }
            if(keywordPass){
                // 롤 필터
                if(role.equals("ALL")){
                    rolePass = true;
                }else {
                    rolePass = user.getRoles().get(user.getRoles().size() - 1).getName().equals(role);
                }
                if(rolePass && keywordPass ){
                    filteredList.add(user);
                }
            }
        }

        Collections.sort(filteredList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if(sortProperty.equals("name")){
                    if(o1.getName().equals(o2.getName())){
                        return 0;
                    }
                    if(sortDirection.equals("desc")){
                        return o2.getName().compareTo(o1.getName());
                    }else{
                        return o1.getName().compareTo(o2.getName());
                    }
                }else if(sortProperty.equals("email")){
                    if(o1.getEmail().equals(o2.getEmail())){
                        return 0;
                    }
                    if(sortDirection.equals("desc")){
                        return o2.getEmail().compareTo(o1.getEmail());
                    }else{
                        return o1.getEmail().compareTo(o2.getEmail());
                    }
                }else if(sortProperty.equals("regDate")){
                    Role role1 = null;
                    Role role2 = null;
                    List<Role> roleList1 = o1.getRoles();
                    List<Role> roleList2 = o2.getRoles();
                    role1 = roleList1.get(roleList1.size() - 1);
                    role2 = roleList2.get(roleList2.size() - 1);
                    if(role1.getCreatedDateTimeFormmat().equals(role2.getCreatedDateTimeFormmat())){
                        return 0;
                    }
                    if(sortDirection.equals("desc")){
                        return role2.getCreatedDateTimeFormmat().compareTo(role1.getCreatedDateTimeFormmat());
                    }else{
                        return role1.getCreatedDateTimeFormmat().compareTo(role2.getCreatedDateTimeFormmat());
                    }
                }else{
                    Role role1 = null;
                    Role role2 = null;
                    List<Role> roleList1 = o1.getRoles();
                    List<Role> roleList2 = o2.getRoles();
                    role1 = roleList1.get(roleList1.size() - 1);
                    role2 = roleList2.get(roleList2.size() - 1);
                    if(role1.getName().equals(role2.getName())){
                        return 0;
                    }
                    if(sortDirection.equals("desc")){
                        return role2.getName().compareTo(role1.getName());
                    }else{
                        return role1.getName().compareTo(role2.getName());
                    }
                }
            }
        });

        List<User> pageList = new ArrayList<User>();
        if(endIdx > filteredList.size()){
            endIdx = filteredList.size();
        }
        // 페이징 필터
        for(int i=startIdx;i<endIdx;i++){
            pageList.add(filteredList.get(i));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total" , filteredList.size());
        map.put("list", pageList);
        return map;
    }

    public User getUser(String id) {
        return userRepo.findById(id).orElse(null);
    }

    /**
     *  enable 상태 변경
     */
    public void updateUserEnabled(String id, boolean enabled){
        User user = userRepo.findById(id).orElse(null);
        user.setEnabled(enabled);
        userRepo.save(user);
    }

    /**
     *  유저 저장
     */
    @Transactional
    public User saveUser(User user, String rolename, boolean isCreate) throws Exception {
        String password = isCreate ? UUID.randomUUID().toString() : user.getPassword();
        if(password != null) {
            // 패스워드 인코더
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 패스워드 인코딩
            String encodePassword   = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
        }

        List<CodeEntity> codeList = codeRepo.findByGroupCodeAndEnabled("ROLE",true);
        if(isCreate){
            // 신규 등록

            user.setDelYn(false);
            user.setName(user.getUsername());
            user.setSuperuser(false);
            user.setEnabled(true);
            if (rolename.equals("ADMIN")) {
                user.setSuperuser(true);
            }

            user =  userRepo.save(user);

            if(rolename.equals("USER")){
                throw new RuntimeException();
            }

            for(CodeEntity code : codeList){
                String insertCodeName = code.getCodeName();
                if((rolename.equals("USER") && insertCodeName.equals(rolename)) || !rolename.equals("USER")){
                    // ADMIN 은 전체 USER는 USER만 등록
                    Role role = new Role();
                    role.setUser(user);
                    role.setName(insertCodeName);
                    role.setStarred(false);
                    try{
                        this.setCreatedInfo(role);
                    }catch(Exception e){}
                    roleRepo.save(role);
                }
            }
        }else{
            User preUser;
            if (user.getUsername() != null) {
                preUser = userRepo.findUserByUsername(user.getUsername());
            } else {
                preUser = userRepo.findById(SecurityUtil.getUser()).orElse(null);
            }

            List<Role> roleList = preUser.getRole();
            // role update
            if (rolename != null) {
                if(roleList.size() == 2 && rolename.equals("USER")){
                    // ADMIN -> USER
                    int idx = 0;
                    for(Role role : roleList){
                        if(role.getName().equals("ADMIN")){
                            // ADMIN권한 회수
                            roleRepo.deleteById(role.getId());
                            preUser.setSuperuser(false);
                            break;
                        }
                        idx++;
                    }
                    roleList.remove(idx);
                    preUser.setRole(roleList);
                }else if(roleList.size() == 1 && rolename.equals("ADMIN")){
                    // USER -> ADMIN
                    Role role = new Role();
                    role.setUser(preUser);
                    role.setName("ADMIN");
                    try{
                        this.setCreatedInfo(role);
                    }catch(Exception e){}
                    // ADMIN 권한 추가
                    role = roleRepo.save(role);
                    roleList.add(role);
                    preUser.setRole(roleList);
                    preUser.setSuperuser(true);
                }
            }

            if(user.getPassword() != null) preUser.setPassword(user.getPassword());
            if(user.getName() != null) preUser.setName(user.getName());
            if(user.getEmail() != null) preUser.setEmail(user.getEmail());
            if(user.getEnabled() != null) preUser.setEnabled(user.getEnabled());
            userRepo.save(preUser);

            user = preUser;
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(password);

        return newUser;

    }

    /**
     * password 비교
     * @param password
     * @return
     * @throws Exception
     */
    public boolean passwordVerify(String password) throws Exception {
        User user = this.getLoginUser();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String p = user.getPassword();
        return bCryptPasswordEncoder.matches(password, p);
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Protected Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Private Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Inner Class
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
}
