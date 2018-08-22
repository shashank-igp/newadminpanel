package com.igp.admin.Voucher.mappers;
import com.igp.admin.Voucher.models.GiftVoucherListModel;
import com.igp.admin.Voucher.models.GiftVoucherModel;
import com.igp.admin.Voucher.models.ResultModel;
import com.igp.admin.Voucher.utils.GiftVoucherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nikhil on 21/08/18.
 */
public class GiftVoucherMapper
{
    private static final Logger logger = LoggerFactory.getLogger(GiftVoucherMapper.class);

    public ResultModel createGiftVoucher(GiftVoucherModel giftVoucherModel){
        GiftVoucherUtil giftVoucherUtil = new GiftVoucherUtil();
        ResultModel result = new ResultModel();

        try{
         /*  List<String> validationList = voucherValidationUtil.createVoucher(giftVoucherModel);
            if(validationList.size() > 0){
                result.setError(true);
                result.setObject(VoucherUtil.getCommaSepString(validationList));
                logger.debug(validationList.toString());
            }else{*/
            if(validateGiftVoucher(giftVoucherModel.getFkAssociateId(), giftVoucherModel.getCouponCode())){
                result.setError(true);
            }
            if(!giftVoucherUtil.createGiftVoucher(giftVoucherModel)){
                result.setError(true);
            }

        }catch (Exception exception){
            logger.debug("error occured while creating gift Voucher "+exception);
            result.setError(true);
        }
        return result;
    }

    public ResultModel updateGiftVoucher(GiftVoucherModel giftVoucherModel){
        GiftVoucherUtil giftVoucherUtil=new GiftVoucherUtil();
        ResultModel result = new ResultModel();

        try{
        /*    List<String> validationList = voucherValidationUtil.updateVoucher(giftVoucherModel);
            if(validationList.size() > 0){
                result.setError(true);
                result.setObject(VoucherUtil.getCommaSepString(validationList));
                logger.debug(validationList.toString());
            }else{*/

            if(!giftVoucherUtil.updateGiftVoucher(giftVoucherModel)){
                result.setError(true);
            }

        }catch (Exception exception){
            logger.debug("error occured while updating gift oucher "+exception);
        }
        return result;
    }

    public ResultModel deleteGiftVoucher(int id, int fkAssociateId){
        GiftVoucherUtil giftVoucherUtil = new GiftVoucherUtil();
        ResultModel result = new ResultModel();

        try{
            /*List<String> validationList = voucherValidationUtil.deleteVoucher(id,modifiedBy);
            if(validationList.size() > 0){
                result.setError(true);
                result.setObject(VoucherUtil.getCommaSepString(validationList));
                logger.debug(validationList.toString());
            }else{*/
                if(!giftVoucherUtil.deleteGiftVoucher(id, fkAssociateId)){
                    result.setError(true);
                }

        }catch (Exception exception){
            logger.debug("error occured while deleting gift voucher "+exception);
        }
        return result;
    }

    public boolean validateGiftVoucher(int fkAssociateId, String voucherCode){

        GiftVoucherUtil giftVoucherUtil = new GiftVoucherUtil();
        boolean result = false;
        try{
            result = giftVoucherUtil.validateGiftVoucher(fkAssociateId,voucherCode);
        }catch (Exception exception){
            logger.debug("error occured while validating gift voucher "+exception);
        }
        return result;
    }

    public GiftVoucherListModel getGiftVoucherList(int id, int fkAssociateId, int start, int end){
        GiftVoucherUtil giftVoucherUtil = new GiftVoucherUtil();
        GiftVoucherListModel giftVoucherModelList = new GiftVoucherListModel();
        try{
           giftVoucherModelList = giftVoucherUtil.getGiftVoucherList(id,fkAssociateId,start,end);
        }catch (Exception exception){
            logger.debug("error occured while getVoucherList. "+exception);
        }
        return giftVoucherModelList;
    }

}
