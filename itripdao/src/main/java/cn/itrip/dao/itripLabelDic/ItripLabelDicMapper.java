package cn.itrip.dao.itripLabelDic;
import cn.itrip.pojo.ItripLabelDic;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripLabelDicMapper {
	//热门酒店测试
	public List<ItripLabelDic> firstTop()throws Exception;

	public ItripLabelDic getItripLabelDicById(@Param(value = "id") Long id)throws Exception;

	public List<ItripLabelDic>	getItripLabelDicListByMap(Map<String,Object> param)throws Exception;

	public Integer getItripLabelDicCountByMap(Map<String,Object> param)throws Exception;

	public Integer insertItripLabelDic(ItripLabelDic itripLabelDic)throws Exception;

	public Integer updateItripLabelDic(ItripLabelDic itripLabelDic)throws Exception;

	public Integer deleteItripLabelDicById(@Param(value = "id") Long id)throws Exception;

}
