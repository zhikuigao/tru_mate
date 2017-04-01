package com.jws.app.operater.data;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.jws.app.operater.model.CategoryRec;




public interface CategoryRecMapper {
	  List<CategoryRec> selectPushInnovationType(@Param("userid")String userid);
	  List<CategoryRec> selectPushAppType(@Param("userid")String userid);
	  List<HashMap<String, Object>> selectPlugInType(@Param("userid")String userid);
}