<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="»î¶¯id" prop="promotionId">
      <el-input v-model="dataForm.promotionId" placeholder="»î¶¯id"></el-input>
    </el-form-item>
    <el-form-item label="»î¶¯³¡´Îid" prop="promotionSessionId">
      <el-input v-model="dataForm.promotionSessionId" placeholder="»î¶¯³¡´Îid"></el-input>
    </el-form-item>
    <el-form-item label="ÉÌÆ·id" prop="skuId">
      <el-input v-model="dataForm.skuId" placeholder="ÉÌÆ·id"></el-input>
    </el-form-item>
    <el-form-item label="ÃëÉ±¼Û¸ñ" prop="seckillPrice">
      <el-input v-model="dataForm.seckillPrice" placeholder="ÃëÉ±¼Û¸ñ"></el-input>
    </el-form-item>
    <el-form-item label="ÃëÉ±×ÜÁ¿" prop="seckillCount">
      <el-input v-model="dataForm.seckillCount" placeholder="ÃëÉ±×ÜÁ¿"></el-input>
    </el-form-item>
    <el-form-item label="Ã¿ÈËÏÞ¹ºÊýÁ¿" prop="seckillLimit">
      <el-input v-model="dataForm.seckillLimit" placeholder="Ã¿ÈËÏÞ¹ºÊýÁ¿"></el-input>
    </el-form-item>
    <el-form-item label="ÅÅÐò" prop="seckillSort">
      <el-input v-model="dataForm.seckillSort" placeholder="ÅÅÐò"></el-input>
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
          promotionId: '',
          promotionSessionId: '',
          skuId: '',
          seckillPrice: '',
          seckillCount: '',
          seckillLimit: '',
          seckillSort: ''
        },
        dataRule: {
          promotionId: [
            { required: true, message: '»î¶¯id不能为空', trigger: 'blur' }
          ],
          promotionSessionId: [
            { required: true, message: '»î¶¯³¡´Îid不能为空', trigger: 'blur' }
          ],
          skuId: [
            { required: true, message: 'ÉÌÆ·id不能为空', trigger: 'blur' }
          ],
          seckillPrice: [
            { required: true, message: 'ÃëÉ±¼Û¸ñ不能为空', trigger: 'blur' }
          ],
          seckillCount: [
            { required: true, message: 'ÃëÉ±×ÜÁ¿不能为空', trigger: 'blur' }
          ],
          seckillLimit: [
            { required: true, message: 'Ã¿ÈËÏÞ¹ºÊýÁ¿不能为空', trigger: 'blur' }
          ],
          seckillSort: [
            { required: true, message: 'ÅÅÐò不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/coupon/seckillskurelation/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.promotionId = data.seckillSkuRelation.promotionId
                this.dataForm.promotionSessionId = data.seckillSkuRelation.promotionSessionId
                this.dataForm.skuId = data.seckillSkuRelation.skuId
                this.dataForm.seckillPrice = data.seckillSkuRelation.seckillPrice
                this.dataForm.seckillCount = data.seckillSkuRelation.seckillCount
                this.dataForm.seckillLimit = data.seckillSkuRelation.seckillLimit
                this.dataForm.seckillSort = data.seckillSkuRelation.seckillSort
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
              url: this.$http.adornUrl(`/coupon/seckillskurelation/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'promotionId': this.dataForm.promotionId,
                'promotionSessionId': this.dataForm.promotionSessionId,
                'skuId': this.dataForm.skuId,
                'seckillPrice': this.dataForm.seckillPrice,
                'seckillCount': this.dataForm.seckillCount,
                'seckillLimit': this.dataForm.seckillLimit,
                'seckillSort': this.dataForm.seckillSort
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
