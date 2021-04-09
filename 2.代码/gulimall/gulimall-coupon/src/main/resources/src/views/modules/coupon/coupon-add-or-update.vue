<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="ÓÅ»Ý¾íÀàÐÍ[0->È«³¡ÔùÈ¯£»1->»áÔ±ÔùÈ¯£»2->¹ºÎïÔùÈ¯£»3->×¢²áÔùÈ¯]" prop="couponType">
      <el-input v-model="dataForm.couponType" placeholder="ÓÅ»Ý¾íÀàÐÍ[0->È«³¡ÔùÈ¯£»1->»áÔ±ÔùÈ¯£»2->¹ºÎïÔùÈ¯£»3->×¢²áÔùÈ¯]"></el-input>
    </el-form-item>
    <el-form-item label="ÓÅ»ÝÈ¯Í¼Æ¬" prop="couponImg">
      <el-input v-model="dataForm.couponImg" placeholder="ÓÅ»ÝÈ¯Í¼Æ¬"></el-input>
    </el-form-item>
    <el-form-item label="ÓÅ»Ý¾íÃû×Ö" prop="couponName">
      <el-input v-model="dataForm.couponName" placeholder="ÓÅ»Ý¾íÃû×Ö"></el-input>
    </el-form-item>
    <el-form-item label="ÊýÁ¿" prop="num">
      <el-input v-model="dataForm.num" placeholder="ÊýÁ¿"></el-input>
    </el-form-item>
    <el-form-item label="½ð¶î" prop="amount">
      <el-input v-model="dataForm.amount" placeholder="½ð¶î"></el-input>
    </el-form-item>
    <el-form-item label="Ã¿ÈËÏÞÁìÕÅÊý" prop="perLimit">
      <el-input v-model="dataForm.perLimit" placeholder="Ã¿ÈËÏÞÁìÕÅÊý"></el-input>
    </el-form-item>
    <el-form-item label="Ê¹ÓÃÃÅ¼÷" prop="minPoint">
      <el-input v-model="dataForm.minPoint" placeholder="Ê¹ÓÃÃÅ¼÷"></el-input>
    </el-form-item>
    <el-form-item label="¿ªÊ¼Ê±¼ä" prop="startTime">
      <el-input v-model="dataForm.startTime" placeholder="¿ªÊ¼Ê±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="½áÊøÊ±¼ä" prop="endTime">
      <el-input v-model="dataForm.endTime" placeholder="½áÊøÊ±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="Ê¹ÓÃÀàÐÍ[0->È«³¡Í¨ÓÃ£»1->Ö¸¶¨·ÖÀà£»2->Ö¸¶¨ÉÌÆ·]" prop="useType">
      <el-input v-model="dataForm.useType" placeholder="Ê¹ÓÃÀàÐÍ[0->È«³¡Í¨ÓÃ£»1->Ö¸¶¨·ÖÀà£»2->Ö¸¶¨ÉÌÆ·]"></el-input>
    </el-form-item>
    <el-form-item label="±¸×¢" prop="note">
      <el-input v-model="dataForm.note" placeholder="±¸×¢"></el-input>
    </el-form-item>
    <el-form-item label="·¢ÐÐÊýÁ¿" prop="publishCount">
      <el-input v-model="dataForm.publishCount" placeholder="·¢ÐÐÊýÁ¿"></el-input>
    </el-form-item>
    <el-form-item label="ÒÑÊ¹ÓÃÊýÁ¿" prop="useCount">
      <el-input v-model="dataForm.useCount" placeholder="ÒÑÊ¹ÓÃÊýÁ¿"></el-input>
    </el-form-item>
    <el-form-item label="ÁìÈ¡ÊýÁ¿" prop="receiveCount">
      <el-input v-model="dataForm.receiveCount" placeholder="ÁìÈ¡ÊýÁ¿"></el-input>
    </el-form-item>
    <el-form-item label="¿ÉÒÔÁìÈ¡µÄ¿ªÊ¼ÈÕÆÚ" prop="enableStartTime">
      <el-input v-model="dataForm.enableStartTime" placeholder="¿ÉÒÔÁìÈ¡µÄ¿ªÊ¼ÈÕÆÚ"></el-input>
    </el-form-item>
    <el-form-item label="¿ÉÒÔÁìÈ¡µÄ½áÊøÈÕÆÚ" prop="enableEndTime">
      <el-input v-model="dataForm.enableEndTime" placeholder="¿ÉÒÔÁìÈ¡µÄ½áÊøÈÕÆÚ"></el-input>
    </el-form-item>
    <el-form-item label="ÓÅ»ÝÂë" prop="code">
      <el-input v-model="dataForm.code" placeholder="ÓÅ»ÝÂë"></el-input>
    </el-form-item>
    <el-form-item label="¿ÉÒÔÁìÈ¡µÄ»áÔ±µÈ¼¶[0->²»ÏÞµÈ¼¶£¬ÆäËû-¶ÔÓ¦µÈ¼¶]" prop="memberLevel">
      <el-input v-model="dataForm.memberLevel" placeholder="¿ÉÒÔÁìÈ¡µÄ»áÔ±µÈ¼¶[0->²»ÏÞµÈ¼¶£¬ÆäËû-¶ÔÓ¦µÈ¼¶]"></el-input>
    </el-form-item>
    <el-form-item label="·¢²¼×´Ì¬[0-Î´·¢²¼£¬1-ÒÑ·¢²¼]" prop="publish">
      <el-input v-model="dataForm.publish" placeholder="·¢²¼×´Ì¬[0-Î´·¢²¼£¬1-ÒÑ·¢²¼]"></el-input>
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
          couponType: '',
          couponImg: '',
          couponName: '',
          num: '',
          amount: '',
          perLimit: '',
          minPoint: '',
          startTime: '',
          endTime: '',
          useType: '',
          note: '',
          publishCount: '',
          useCount: '',
          receiveCount: '',
          enableStartTime: '',
          enableEndTime: '',
          code: '',
          memberLevel: '',
          publish: ''
        },
        dataRule: {
          couponType: [
            { required: true, message: 'ÓÅ»Ý¾íÀàÐÍ[0->È«³¡ÔùÈ¯£»1->»áÔ±ÔùÈ¯£»2->¹ºÎïÔùÈ¯£»3->×¢²áÔùÈ¯]不能为空', trigger: 'blur' }
          ],
          couponImg: [
            { required: true, message: 'ÓÅ»ÝÈ¯Í¼Æ¬不能为空', trigger: 'blur' }
          ],
          couponName: [
            { required: true, message: 'ÓÅ»Ý¾íÃû×Ö不能为空', trigger: 'blur' }
          ],
          num: [
            { required: true, message: 'ÊýÁ¿不能为空', trigger: 'blur' }
          ],
          amount: [
            { required: true, message: '½ð¶î不能为空', trigger: 'blur' }
          ],
          perLimit: [
            { required: true, message: 'Ã¿ÈËÏÞÁìÕÅÊý不能为空', trigger: 'blur' }
          ],
          minPoint: [
            { required: true, message: 'Ê¹ÓÃÃÅ¼÷不能为空', trigger: 'blur' }
          ],
          startTime: [
            { required: true, message: '¿ªÊ¼Ê±¼ä不能为空', trigger: 'blur' }
          ],
          endTime: [
            { required: true, message: '½áÊøÊ±¼ä不能为空', trigger: 'blur' }
          ],
          useType: [
            { required: true, message: 'Ê¹ÓÃÀàÐÍ[0->È«³¡Í¨ÓÃ£»1->Ö¸¶¨·ÖÀà£»2->Ö¸¶¨ÉÌÆ·]不能为空', trigger: 'blur' }
          ],
          note: [
            { required: true, message: '±¸×¢不能为空', trigger: 'blur' }
          ],
          publishCount: [
            { required: true, message: '·¢ÐÐÊýÁ¿不能为空', trigger: 'blur' }
          ],
          useCount: [
            { required: true, message: 'ÒÑÊ¹ÓÃÊýÁ¿不能为空', trigger: 'blur' }
          ],
          receiveCount: [
            { required: true, message: 'ÁìÈ¡ÊýÁ¿不能为空', trigger: 'blur' }
          ],
          enableStartTime: [
            { required: true, message: '¿ÉÒÔÁìÈ¡µÄ¿ªÊ¼ÈÕÆÚ不能为空', trigger: 'blur' }
          ],
          enableEndTime: [
            { required: true, message: '¿ÉÒÔÁìÈ¡µÄ½áÊøÈÕÆÚ不能为空', trigger: 'blur' }
          ],
          code: [
            { required: true, message: 'ÓÅ»ÝÂë不能为空', trigger: 'blur' }
          ],
          memberLevel: [
            { required: true, message: '¿ÉÒÔÁìÈ¡µÄ»áÔ±µÈ¼¶[0->²»ÏÞµÈ¼¶£¬ÆäËû-¶ÔÓ¦µÈ¼¶]不能为空', trigger: 'blur' }
          ],
          publish: [
            { required: true, message: '·¢²¼×´Ì¬[0-Î´·¢²¼£¬1-ÒÑ·¢²¼]不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/coupon/coupon/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.couponType = data.coupon.couponType
                this.dataForm.couponImg = data.coupon.couponImg
                this.dataForm.couponName = data.coupon.couponName
                this.dataForm.num = data.coupon.num
                this.dataForm.amount = data.coupon.amount
                this.dataForm.perLimit = data.coupon.perLimit
                this.dataForm.minPoint = data.coupon.minPoint
                this.dataForm.startTime = data.coupon.startTime
                this.dataForm.endTime = data.coupon.endTime
                this.dataForm.useType = data.coupon.useType
                this.dataForm.note = data.coupon.note
                this.dataForm.publishCount = data.coupon.publishCount
                this.dataForm.useCount = data.coupon.useCount
                this.dataForm.receiveCount = data.coupon.receiveCount
                this.dataForm.enableStartTime = data.coupon.enableStartTime
                this.dataForm.enableEndTime = data.coupon.enableEndTime
                this.dataForm.code = data.coupon.code
                this.dataForm.memberLevel = data.coupon.memberLevel
                this.dataForm.publish = data.coupon.publish
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
              url: this.$http.adornUrl(`/coupon/coupon/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'couponType': this.dataForm.couponType,
                'couponImg': this.dataForm.couponImg,
                'couponName': this.dataForm.couponName,
                'num': this.dataForm.num,
                'amount': this.dataForm.amount,
                'perLimit': this.dataForm.perLimit,
                'minPoint': this.dataForm.minPoint,
                'startTime': this.dataForm.startTime,
                'endTime': this.dataForm.endTime,
                'useType': this.dataForm.useType,
                'note': this.dataForm.note,
                'publishCount': this.dataForm.publishCount,
                'useCount': this.dataForm.useCount,
                'receiveCount': this.dataForm.receiveCount,
                'enableStartTime': this.dataForm.enableStartTime,
                'enableEndTime': this.dataForm.enableEndTime,
                'code': this.dataForm.code,
                'memberLevel': this.dataForm.memberLevel,
                'publish': this.dataForm.publish
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
