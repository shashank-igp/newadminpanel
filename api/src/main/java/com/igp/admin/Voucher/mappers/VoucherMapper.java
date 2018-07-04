package com.igp.admin.Voucher.mappers;
import com.igp.admin.Blogs.models.SeoBlogModel;
import com.igp.admin.Voucher.models.*;
import com.igp.admin.Voucher.utils.VoucherUtil;
import com.igp.admin.Voucher.utils.VoucherValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by suditi on 8/6/18.
 */
public class VoucherMapper {
    private static final Logger logger = LoggerFactory.getLogger(VoucherMapper.class);

    public ResultModel createVoucher(VoucherModel voucherModel){
        VoucherUtil voucherUtil = new VoucherUtil();
        ResultModel result = new ResultModel();
        VoucherValidationUtil voucherValidationUtil = new VoucherValidationUtil();
        try{
            List<String> validationList = voucherValidationUtil.createVoucher(voucherModel);
            if(validationList.size() > 0){
                result.setError(true);
                result.setObject(validationList);
                logger.debug(validationList.toString());
            }else{
                if(!voucherUtil.createVoucher(voucherModel)){
                    result.setError(true);
                }
            }
        }catch (Exception exception){
            logger.debug("error occured while creating Voucher "+exception);
            result.setError(true);
        }
        return result;
    }

    public ResultModel updateVoucher(VoucherModel voucherModel){
        VoucherUtil voucherUtil=new VoucherUtil();
        ResultModel result = new ResultModel();
        VoucherValidationUtil voucherValidationUtil = new VoucherValidationUtil();
        try{
            List<String> validationList = voucherValidationUtil.updateVoucher(voucherModel);
            if(validationList.size() > 0){
                result.setError(true);
                result.setObject(validationList);
                logger.debug(validationList.toString());
            }else{
                if(!voucherUtil.updateVoucher(voucherModel)){
                    result.setError(true);
                }
            }
        }catch (Exception exception){
            logger.debug("error occured while updating Voucher "+exception);
        }
        return result;
    }

    public ResultModel deleteVoucher(int id, String modifiedBy){
        VoucherUtil voucherUtil=new VoucherUtil();
        ResultModel result = new ResultModel();
        VoucherValidationUtil voucherValidationUtil = new VoucherValidationUtil();
        try{
            List<String> validationList = voucherValidationUtil.deleteVoucher(id,modifiedBy);
            if(validationList.size() > 0){
                result.setError(true);
                result.setObject(validationList);
                logger.debug(validationList.toString());
            }else{
                if(!voucherUtil.deleteVoucher(id,modifiedBy)){
                    result.setError(true);
                }
            }
        }catch (Exception exception){
            logger.debug("error occured while deleting Voucher "+exception);
        }
        return result;
    }

    public boolean validateVoucher(int fkAssociateId, String voucherCode){

        VoucherUtil voucherUtil=new VoucherUtil();
        boolean result = false;
        try{
            result = voucherUtil.validateVoucher(fkAssociateId,voucherCode);
        }catch (Exception exception){
            logger.debug("error occured while validating Voucher "+exception);
        }
        return result;
    }
    public VoucherListModel getVoucherList(int id, int fkAssociateId, int start, int end){
        VoucherUtil voucherUtil=new VoucherUtil();
        VoucherListModel voucherModelList = new VoucherListModel();
        try{
           voucherModelList = voucherUtil.getVoucherList(id,fkAssociateId,start,end);
        }catch (Exception exception){
            logger.debug("error occured while getVoucherList. "+exception);
        }
        return voucherModelList;
    }
    public List<String> getExistingBlackListProdCats(){
        VoucherUtil voucherUtil=new VoucherUtil();
        List<String> blackList = null;
        try{
            blackList = voucherUtil.getExistingBlackListProdCats();
        }catch (Exception exception){
            logger.debug("error occured while getVoucherList. "+exception);
        }
        return blackList;
    }
    public List<SeoBlogModel> getAssociates(){
        VoucherUtil voucherUtil=new VoucherUtil();
        List<SeoBlogModel> associateList = null;
        try{
            associateList = voucherUtil.getAssociates();
        }catch (Exception exception){
            logger.debug("error occured while getVoucherList. "+exception);
        }
        return associateList;
    }
    public VoucherMetaData getVoucherMetaData(){
        VoucherUtil voucherUtil=new VoucherUtil();
        VoucherMetaData voucherMetaData = null;
        try{
            voucherMetaData = voucherUtil.getVoucherMetaData();
        }catch (Exception exception){
            logger.debug("error occured while getVoucherList. "+exception);
        }
        return voucherMetaData;
    }
    public List<CategoriesModel> getCategories(int parentCatId, int categoryId){
        VoucherUtil voucherUtil=new VoucherUtil();
        List<CategoriesModel> categoriesModelList = null;
        try{
            categoriesModelList = voucherUtil.getCategories(parentCatId, categoryId);
        }catch (Exception exception){
            logger.debug("error occured while getCategories. "+exception);
        }
        return categoriesModelList;
    }

}
