package net.bytecms.system.service.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.PasswordGenerator;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.system.api.system.*;
import net.bytecms.system.dto.system.OrgDto;
import net.bytecms.system.dto.system.UserDto;
import net.bytecms.system.dto.system.UserRoleDto;
import net.bytecms.system.entity.system.UserEntity;
import net.bytecms.system.mapper.system.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserDto, UserEntity, UserMapper> implements UserService {

    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    MenuService menuService;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    OrgService orgService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    BaseRedisService baseRedisService;

    @Override
    public UserDto findUserByUsername(String username) {
        QueryWrapper<UserEntity> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UserEntity userEntity=getOne(queryWrapper);
        return Checker.BeNotNull(userEntity)? T2D(userEntity):null ;// 查询用户账户信息
    }

    @Override
    @Transactional
    public boolean save(UserDto userDto) {
        userDto.setId(generateId());
        handUser(userDto);
        if (super.insert(userDto)) {
            insertUserRole(userDto.getRoleIds(), userDto.getId());
        }
        return true;
    }

    private void handUser(UserDto userDto){
        if (Checker.BeNotBlank(userDto.getOrgId())){
            OrgDto orgDto=orgService.getByPk(userDto.getOrgId());
            if(Checker.BeNotNull(orgDto)){
                userDto.setOrgCode(orgDto.getOrgCode()).setOrgName(orgDto.getOrgName());
            }
        }
        //checker
        checkUserIsExist(userDto);
    }

    private void checkUserIsExist(UserDto userDto){
        UserDto userEmail= getByField("email",userDto.getEmail());
        UserDto username= getByField("username",userDto.getUsername());
        if(Checker.BeNotBlank(userDto.getId())){//编辑
            if(Checker.BeNotNull(userEmail)&& !userEmail.getId().equals(userDto.getId())){
                throw new CustomException(ApiResult.result(5006));
            }
            if(Checker.BeNotNull(username)&& !username.getId().equals(userDto.getId())){
                throw new CustomException(ApiResult.result(5007));
            }
        }else{//新增
            if(Checker.BeNotNull(userEmail)){
                throw new CustomException(ApiResult.result(5006));
            }
            if(Checker.BeNotNull(username)){
                throw new CustomException(ApiResult.result(5007));
            }
        }
    }

    @Override
    public boolean deleteByUserId(String userId) {
        if (userId.equals(getUserId())|| "1".equals(userId)) {
            throw new CustomException(ApiResult.result(5000));
        }
        userRoleService.deleteByUserId(userId);
        return super.removeById(userId);
    }


    @Override
    public boolean update(UserDto userDto) {
        handUser(userDto);
        List<UserRoleDto> list = new ArrayList<UserRoleDto>();
        if (super.updateByPk(userDto)) {
            //根据用户id先删除所有的角色然后再分配
            Map<String, Object> param = new HashMap<>();
            param.put("user_id", userDto.getId());
            userRoleService.removeByMap(param);
            insertUserRole(userDto.getRoleIds(), userDto.getId());
        }
        return true;
    }

    private boolean insertUserRole(String[] roleIds, String userId) {
        List<UserRoleDto> list = new ArrayList<UserRoleDto>();
        for (String roleId : roleIds) {
            UserRoleDto userRole = new UserRoleDto();
            userRole.setRoleId(roleId).setUserId(userId).setId(generateId());
            list.add(userRole);
        }
        boolean res = userRoleService.insertUserRoleBatch(list);
        return res;
    }

    @Override
    public boolean deleteByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        for (String userId : ids) {
            userRoleService.deleteByUserId(userId);
        }
        return super.deleteByPks(ids);
    }

    @Override
    public boolean lockUsers(boolean justLock) {
        String pass=PasswordGenerator.genPass(12);
        pass=bCryptPasswordEncoder.encode(PasswordGenerator.genPass(12));
        return baseMapper.lockUsers(pass,justLock);
    }

    private boolean updateByIds(List<String> ids, Integer type) {
        if (ids == null || ids.isEmpty()) {
            return true;
        }
        List<UserEntity> users = (List) listByIds(ids);
        if (Checker.BeNotEmpty(users)) {
            users.forEach(userEntity -> {
                userEntity.setStatus(type);
            });
            return updateBatchById(users);
        }
        return true;
    }

    @Override
    public UserDto info(String userId) {
        UserDto userDto = getByPk(userId);
        Set<String> perms = menuService.selectPermsByUid(userId);
        userDto.setPerms(perms);
        boolean hasKey=baseRedisService.hasKey(SecurityConstants.LOGIN_DATE_FRIST);
        if(!hasKey){
            baseRedisService.set(SecurityConstants.LOGIN_DATE_FRIST,new Date(), TimeUnit.DAYS.toSeconds(10));
        }
        return userDto;
    }

    @Override
    public UserDto getById(String id) {
        UserDto userDto = getByPk(id);
        List<String> list = userRoleService.selectRoleIdByUId(userDto.getId());
        if (Checker.BeNotEmpty(list)) {
            userDto.setRoleIds(list.toArray(new String[list.size()]));
        }
        return userDto;
    }

    @Override
    public boolean batch(Integer type, List<String> ids) {
        if (type == -1) {
            return deleteByIds(ids);
        } else {
            return updateByIds(ids, type);
        }
    }

    @Override
    public boolean updateUserInfo(UserDto userDto) {
        userDto.setId(getUserId());
        return super.updateByPk(userDto);
    }

    @Override
    public List<UserDto> getUserByOrgId(String orgId) {
        UserDto userDto = new UserDto();
        userDto.setOrgId(orgId);
        List<UserDto> userDtos = listDto(userDto);
        if (Checker.BeEmpty(userDtos)) {
            return Lists.newArrayList();
        }
        return userDtos;
    }

    @Override
    public void modifyPass(UserDto user) {
        UserDto userDto=getByPk(getUserId());
        if(!bCryptPasswordEncoder.matches(user.getInitPass(),userDto.getPassword())){
            throw new CustomException(ApiResult.result(5005));
        }
        userDto.setPassword(bCryptPasswordEncoder.encode(user.getNewPass()));
        updateByPk(userDto);
    }

    @Override
    public ApiResult resetPass(String id) {
        UserDto userDto=getByPk(id);
        if(Checker.BeNull(userDto)||"1".equals(userDto.getId())){
            throw new CustomException(ApiResult.result(7002));
        }
        String pass= PasswordGenerator.genPass(8);
        userDto.setPassword(bCryptPasswordEncoder.encode(pass));
        updateByPk(userDto);
        baseRedisService.remove(SecurityConstants.ERROR_INPUT_PASS+userDto.getUsername());
        return ApiResult.result(pass);
    }

    @Override
    public Set<String> selectRoleSignByUserId(String uid) {
        Set<String> signs=baseMapper.selectRoleSignByUserId(uid);
        return Checker.BeNotEmpty(signs)?signs:new HashSet<>(16);
    }
//
//	@Override
//	public UserEntity selectDetailById(Long userId) {
//		UserEntity user=getById(userId);
//		List<Long> roleIds=userRoleService.selectRoleIdByUId(userId);
//		Long[] roleArr = new Long[roleIds.size()];
//		user.setRoleIds(roleIds.toArray(roleArr));
//		return user;
//	}
//
//	@Override
//	public R updateMyPass(PassWordDto passWordDto) {
//		UserEntity userEntity=getById(passWordDto.getUserId());
//		if(userEntity!=null){
//			String old="";//MD5Utils.encrypt(userEntity.getUsername(), passWordDto.getOldPass());
//			if(old.equals(userEntity.getPassword())){
//				if(passWordDto.getNewPass().equals(passWordDto.getNewPassTwo())){
//					String newP="";//MD5Utils.encrypt(userEntity.getUsername(), passWordDto.getNewPass());
//					if(newP.equals(old)){
//						return R.error("新密码和旧密码一致,请确认");
//					}else{
//						userEntity.setPassword(newP);
//						if(updateById(userEntity)){
//							return R.ok("更新成功!");
//						}else{
//							return R.error("更新失败，请联系管理员");
//						}
//					}
//
//				}else{//不一致
//					return R.error("2次新密码不一致，请确认");
//				}
//			}else{
//				return R.error("旧密码不正确,请确认");
//			}
//		}else{
//			return R.error("用户不存在");
//		}
//	}
//
//	@Override
//	public String selectRoleName(Long userId) {
//		return userRoleDao.selectRoleName(userId);
//	}
//
//	@Override
//	public List<UserEntity> selectUserByDeptId(Long deptId) {
//		Map<String, Object> param=new HashMap<>();
//		param.put("dept_id", deptId);
//		return (List<UserEntity>) listByMap(param);
//	}
//
//	@Override
//	public List<String> selectRoleSign(Long userId) {
//		return userRoleDao.selectRoleSign(userId);
//	}
//
//	@Override
//	public R updateUserPass(Long userId) {
//		UserEntity user=getById(userId);
//		if(user!=null) {
//			String newp="";//MD5Utils.encrypt(user.getUsername(),Constant.newpass);
//			user.setPassword(newp);
//			if(updateById(user)) {
//				RedisTemplateUtil.del(Constant.loginTimes+user.getUsername());
//				return R.ok("修改成功! 新的密码为："+Constant.newpass);
//			}else {
//				return R.error("修改用户密码失败!");
//			}
//		}else {
//			return R.error("修改用户密码失败!");
//		}
//	}
//
//	@Override
//	public void buildEmailLink(UserEntity user, HttpServletRequest request) {
//		//生成密钥
//		String secretKey=UUID.randomUUID().toString();
//		//设置过期时间
//		Date outDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000);// 30分钟后过期
//		long date = outDate.getTime() / 1000 * 1000;// 忽略毫秒数  mySql 取出时间是忽略毫秒数的
//		//此处应该更新user表中的过期时间、密钥信息
//		user.setOutDate(date);
//		user.setSecretKey(secretKey);
//		this.updateById(user);
//		//将用户名、过期时间、密钥生成链接密钥
//		String key =user.getEmail() + "$" + date + "$" + secretKey;
//		String digitalSignature = "";//MD5Utils.encrypt(key);// 数字签名
//		String path=request.getContextPath();
//		int port=request.getServerPort();
//		String basePath=request.getScheme()+"://"+request.getServerName()+":"+port+path;
//		if(port==80)
//			basePath=request.getScheme()+"://"+request.getServerName()+path;
//		String resetPassHref = basePath+"/email/toResetPass?sid="+digitalSignature+"&mail="+user.getEmail();
//		sendMail(user,resetPassHref);
//
//	}
//
//	private void sendMail(UserEntity user,String url){
//		Map<String, Object> map=new HashMap<>();
//		map.put("url", url);
//		String[] mails=new String[]{user.getEmail()};
//		MailData mail=new MailData();
//		mail.setPropertyMap(map);
//		mail.setUserIdCreate(user.getUserId());
//		mail.setTo(mails);
//		mail.setTplId("1");
//		messageService.sendAsynMail(mail);
//	}
//
//	@Override
//	public Map<String, Object> checkEmailLink(Map<String, String> map) {
//		Map<String, Object> res=new HashMap<>();
//		res.put("valid", false);
//		res.put("user", new UserEntity());
//		String sid=map.get("sid");
//		String mail=map.get("mail");
//		//获取链接中的用户名
//		if(StringUtils.isBlank(sid)||StringUtils.isBlank(mail)){
//			res.put("msg", "请求的链接不正确,请重新操作.");
//			return res;
//		}
//		UserEntity user= selectByEmail(mail);
//		if(user!=null){
//			//获取当前用户申请找回密码的过期时间
//			//找回密码链接已经过期
//			if(user.getOutDate()==null) {
//				res.put("msg", "该链接不正确,请确认是否是最新链接!");
//				return res;
//			}
//			if(user.getOutDate()<=System.currentTimeMillis()){
//				if(user.getOutDate().longValue()==-1){
//					res.put("msg", "该链接已被验证,请重新发送!");
//				}else{
//					res.put("msg", "链接已经过期请重新发送!");
//				}
//				return res;
//			}
//			//获取当前登陆人的加密码
//			String key = user.getEmail()+"$"+user.getOutDate()/1000*1000+"$"+user.getSecretKey();//数字签名
//			String digitalSignature ="";// MD5Utils.encrypt(key);// 数字签名
//			if(!digitalSignature.equals(sid)){
//				res.put("msg", "该链接不正确,请确认是否是最新链接!");
//				return res;
//			}else{
//				res.put("valid", true);
//				res.put("user", user);
//				return res;
//			}
//		}else{
//			res.put("msg", "用户信息不存在.");
//			return res;
//		}
//	}
//
//	@Override
//	public UserEntity selectByEmail(String email) {
//		QueryWrapper<UserEntity> wrapper = new QueryWrapper<UserEntity>();
//		wrapper.eq("email", email);
//		UserEntity user= this.getOne(wrapper);
//		return user;
//	}
//

}
