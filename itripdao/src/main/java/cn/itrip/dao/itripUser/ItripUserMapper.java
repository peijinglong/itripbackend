package cn.itrip.dao.itripUser;
import cn.itrip.pojo.ItripUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripUserMapper {
	//注册新用户测试
	public Integer insertItripUserCS(ItripUser itripUser)throws Exception;
	//验证用户名是否重复
	public List<ItripUser> getItripUserByUserName(@Param(value = "userCode") String userCode)throws Exception;

	public ItripUser getItripUserById(@Param(value = "id") Long id)throws Exception;

	public ItripUser getItripUserListByMap(Map param)throws Exception;

	public Integer getItripUserCountByMap(Map<String,Object> param)throws Exception;

	public Integer insertItripUser(ItripUser itripUser)throws Exception;

	public Integer updateItripUser(ItripUser itripUser)throws Exception;

	public Integer deleteItripUserById(@Param(value = "id") Long id)throws Exception;

}
