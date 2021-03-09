package net.bytecms.web.controller.system;

import net.bytecms.core.model.PageDto;
import net.bytecms.system.api.system.OrgService;
import net.bytecms.system.api.system.RoleService;
import net.bytecms.system.api.system.UserRoleService;
import net.bytecms.system.dto.system.OrgDto;
import net.bytecms.core.enumerate.LogModule;
import net.bytecms.core.enumerate.LogOperation;
import net.bytecms.core.annotation.Logs;
import net.bytecms.core.utils.Tree;
import net.bytecms.web.controller.BaseController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

@Validated
@RequestMapping(value={"org"})
@RestController
public class OrgController extends BaseController<OrgService> {

	@Resource
	private RoleService roleService;
	
	@Resource
	private UserRoleService userRoleService;
	
	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	@Logs(module = LogModule.ORG,operaEnum = LogOperation.VIEW)
	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public PageDto<OrgDto> list(@RequestBody  PageDto<OrgDto> pageDto) {
		return service.listPage(pageDto);
	}


	@Logs(module = LogModule.ORG,operaEnum = LogOperation.SAVE)
	@RequestMapping(value="save",method=RequestMethod.POST)
	public void save(@Validated @RequestBody  OrgDto orgDto) {
		service.saveOrg(orgDto);
    }

	@RequestMapping(value="info",method = RequestMethod.GET)
	public OrgDto info(@NotBlank @RequestParam String id){
		return service.info(id);
	}

	@RequestMapping("/selectTreeList")
	public Tree<OrgDto> selectTreeList(){
		Tree<OrgDto> menus=service.selectTreeList();
		return menus;
	}


	@Logs(module = LogModule.ORG,operaEnum = LogOperation.UPDATE)
	@RequestMapping(value="update",method=RequestMethod.PUT)
	public void update(@Validated @RequestBody OrgDto orgDto) {
		service.updateByPk(orgDto);
	}


	@Logs(module = LogModule.ORG,operaEnum = LogOperation.DELETE)
	@DeleteMapping(value="delete")
	public boolean delByPk(@NotBlank @RequestParam String id){
		return service.deleteOrg(id);
	}

}
