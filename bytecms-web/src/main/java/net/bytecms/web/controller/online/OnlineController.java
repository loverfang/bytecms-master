package net.bytecms.web.controller.online;

import net.bytecms.core.model.PageDto;
import net.bytecms.system.api.online.OnlineService;
import net.bytecms.system.dto.online.OnlineDto;
import net.bytecms.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dl
 * @since 2018-03-28
 */
@RestController
@RequestMapping("/online")
public class OnlineController extends BaseController<OnlineService> {

	@RequestMapping("/page")
	public PageDto<OnlineDto> selectList(@RequestBody PageDto<OnlineDto> dto){
		service.initTable();
		return service.listPage(dto);
	}
	
	@GetMapping("/info")
	public OnlineDto info(@RequestParam Long id){
		return service.getByPk(id);
	}
	
	@PutMapping("/update")
	public void update(@RequestBody OnlineDto online){
		service.updateByPk(online);
	}
	
	/** 
	* @Description: 根据模板生成代码
	* @param @param id  
	* @return void 
	* @throws 
	*/ 
	@PutMapping("/down")
	public void down(Long id,HttpServletRequest request){
		OnlineDto online=service.getByPk(id);
		if(online!=null){
			service.down(online,request);
		}
	}

	@PostMapping("/searchAuthor")
	public List<OnlineDto> searchAuthor(@RequestBody OnlineDto online){
		return service.listDto(online);
	}


	@DeleteMapping("/delete")
	public void deleteRow(@RequestParam Long id){
		service.deleteByPk(id);
	}
}
