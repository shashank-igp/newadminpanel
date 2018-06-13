package com.igp.admin.Voucher.mappers;
import com.igp.admin.Voucher.models.VoucherListModel;
import com.igp.admin.Voucher.models.VoucherModel;
import com.igp.admin.Voucher.utils.VoucherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by suditi on 8/6/18.
 */
public class VoucherMapper {
    private static final Logger logger = LoggerFactory.getLogger(VoucherMapper.class);

    public boolean createVoucher(VoucherModel voucherModel){
        boolean result = false;
        VoucherUtil voucherUtil = new VoucherUtil();
        try{
            result = voucherUtil.createVoucher(voucherModel);
        }catch (Exception exception){
            logger.debug("error occured while creating Voucher "+exception);
        }
        return result;
    }

    public boolean updateVoucher(VoucherModel voucherModel){

        VoucherUtil voucherUtil=new VoucherUtil();
        boolean result = false;
        try{
            result = voucherUtil.updateVoucher(voucherModel);
        }catch (Exception exception){
            logger.debug("error occured while updating Voucher "+exception);
        }
        return result;
    }

    public boolean deleteVoucher(int id, String modifiedBy){

        boolean result = false;
        VoucherUtil voucherUtil=new VoucherUtil();
        try{
            result = voucherUtil.deleteVoucher(id,modifiedBy);
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
    
}
