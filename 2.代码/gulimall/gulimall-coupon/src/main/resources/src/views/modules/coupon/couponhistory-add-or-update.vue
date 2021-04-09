<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="ÓÅ»ÝÈ¯id" prop="couponId">
      <el-input v-model="dataForm.couponId" placeholder="ÓÅ»ÝÈ¯id"></el-input>
    </el-form-item>
    <el-form-item label="»áÔ±id" prop="memberId">
      <el-input v-model="dataForm.memberId" placeholder="»áÔ±id"></el-input>
    </el-form-item>
    <el-form-item label="»áÔ±Ãû×Ö" prop="memberNickName">
      <el-input v-model="dataForm.memberNickName" placeholder="»áÔ±Ãû×Ö"></el-input>
    </el-form-item>
    <el-form-item label="»ñÈ¡·½Ê½[0->ºóÌ¨ÔùËÍ£»1->Ö÷¶¯ÁìÈ¡]" prop="getType">
      <el-input v-model="dataForm.getType" placeholder="»ñÈ¡·½Ê½[0->ºóÌ¨ÔùËÍ£»1->Ö÷¶¯ÁìÈ¡]"></el-input>
    </el-form-item>
    <el-form-item label="´´½¨Ê±¼ä" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="´´½¨Ê±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="Ê¹ÓÃ×´Ì¬[0->Î´Ê¹ÓÃ£»1->ÒÑÊ¹ÓÃ£»2->ÒÑ¹ýÆÚ]" prop="useType">
      <el-input v-model="dataForm.useType" placeholder="Ê¹ÓÃ×´Ì¬[0->Î´Ê¹ÓÃ£»1->ÒÑÊ¹ÓÃ£»2->ÒÑ¹ýÆÚ]"></el-input>
    </el-form-item>
    <el-form-item label="Ê¹ÓÃÊ±¼ä" prop="useTime">
      <el-input v-model="dataForm.useTime" placeholder="Ê¹ÓÃÊ±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="¶©µ¥id" prop="orderId">
      <el-input v-model="dataForm.orderId" placeholder="¶©µ¥id"></el-input>
    </el-form-item>
    <el-form-item label="¶©µ¥ºÅ" prop="orderSn">
      <el-input v-model="dataForm.orderSn" placeholder="¶©µ¥ºÅ"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          couponId: '',
          memberId: '',
          memberNickName: '',
          getType: '',
          createTime: '',
          useType: '',
          useTime: '',
          orderId: '',
          orderSn: ''
        },
        dataRule: {
          couponId: [
            { required: true, message: 'ÓÅ»ÝÈ¯id不能为空', trigger: 'blur' }
          ],
          memberId: [
            { required: true, message: '»áÔ±id不能为空', trigger: 'blur' }
          ],
          memberNickName: [
            { required: true, message: '»áÔ±Ãû×Ö不能为空', trigger: 'blur' }
          ],
          getType: [
            { required: true, message: '»ñÈ¡·½Ê½[0->ºóÌ¨ÔùËÍ£»1->Ö÷¶¯ÁìÈ¡]不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '´´½¨Ê±¼ä不能为空', trigger: 'blur' }
          ],
          useType: [
            { required: true, message: 'Ê¹ÓÃ×´Ì¬[0->Î´Ê¹ÓÃ£»1->ÒÑÊ¹ÓÃ£»2->ÒÑ¹ýÆÚ]不能为空', trigger: 'blur' }
          ],
          useTime: [
            { required: true, message: 'Ê¹ÓÃÊ±¼ä不能为空', trigger: 'blur' }
          ],
          orderId: [
            { required: true, message: '¶©µ¥id不能为空', trigger: 'blur' }
          ],
          orderSn: [
            { required: true, message: '¶©µ¥ºÅ不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/coupon/couponhistory/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.couponId = data.couponHistory.couponId
                this.dataForm.memberId = data.couponHistory.memberId
                this.dataForm.memberNickName = data.couponHistory.memberNickName
                this.dataForm.getType = data.couponHistory.getType
                this.dataForm.createTime = data.couponHistory.createTime
                this.dataForm.useType = data.couponHistory.useType
                this.dataForm.useTime = data.couponHistory.useTime
                this.dataForm.orderId = data.couponHistory.orderId
                this.dataForm.orderSn = data.couponHistory.orderSn
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/coupon/couponhistory/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'couponId': this.dataForm.couponId,
                'memberId': this.dataForm.memberId,
                'memberNickName': this.dataForm.memberNickName,
                'getType': this.dataForm.getType,
                'createTime': this.dataForm.createTime,
                'useType': this.dataForm.useType,
                'useTime': this.dataForm.useTime,
                'orderId': this.dataForm.orderId,
                'orderSn': this.dataForm.orderSn
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
