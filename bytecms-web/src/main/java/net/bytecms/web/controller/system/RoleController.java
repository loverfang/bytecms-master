package net.bytecms.web.controller.system;

import net.bytecms.core.model.PageDto;
import net.bytecms.system.api.system.RoleService;
import net.bytecms.system.dto.system.RoleDto;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author DL
 * @since 2018-03-19
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<RoleService> {
    private String prefix = "system/role/";


    @RequestMapping("page")
    public PageDto<RoleDto> page(@RequestBody PageDto<RoleDto> roleDto) {
        if(!"1".equals(getUserId())){
            roleDto.getDto().condition().notIn("id","1");
        }
        return service.listPage(roleDto);
    }


    @PostMapping("/save")
    public boolean save(@RequestBody RoleDto role) {
        return service.insert(role);
    }

    @PostMapping("/update")
    public boolean update(@RequestBody @Valid RoleDto role) {
        return service.updateByPk(role);
    }


    @DeleteMapping("/delete")
    public ApiResult deleteRow(@RequestParam String id) {
        return service.deleteById(id);
    }

    /**
     * @param @return
     * @return List<Role>
     * @throws
     * @Description: 添加用户时需要选择用户角色此处查询所有角色
     */
    @RequestMapping("/selectAllRoles")
    public List<RoleDto> selectAllRoles() {
        return service.selectAllRoles();
    }


//	@PostMapping("/checkIsExist")
//	public boolean checkIsExist(Role roleEntity){
//		Long roleId=roleEntity.getRoleId();
//		roleEntity.setRoleId(null);
//		QueryWrapper<Role> query = new QueryWrapper<Role>(roleEntity);
//		Role role=service.getOne(query);
//		if(role!=null){
//		    if(roleId!=null){//编辑时
//                  if(role.getRoleId().intValue()==roleId.intValue()){
//                     return true;
//                  }else{
//                      return false;
//                  }
//            }else{
//                return false;
//            }
//		}else{
//			return true;
//		}
//	}

}
