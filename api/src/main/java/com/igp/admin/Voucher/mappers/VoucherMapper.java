package com.igp.admin.Voucher.mappers;
import com.igp.admin.Voucher.models.VoucherModel;
import com.igp.admin.Voucher.utils.VoucherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

    public boolean deleteVoucher(int id){

        boolean result = false;
        VoucherUtil voucherUtil=new VoucherUtil();
        try{
            result = voucherUtil.deleteVoucher(id);
        }catch (Exception exception){
            logger.debug("error occured while deleting Voucher "+exception);
        }
        return result;
    }

    
    public List<VoucherModel> getVoucherList(int id, int start, int end){
        VoucherUtil voucherUtil=new VoucherUtil();
        List<VoucherModel> voucherModelList = new ArrayList<>();
        try{
            voucherModelList = voucherUtil.getVoucherList(id,start,end);
        }catch (Exception exception){
            logger.debug("error occured while getVoucherList. "+exception);
        }
        return voucherModelList;
    }
    
}
