<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="spu_id" prop="skuId">
      <el-input v-model="dataForm.skuId" placeholder="spu_id"></el-input>
    </el-form-item>
    <el-form-item label="Âú¼¸¼þ" prop="fullCount">
      <el-input v-model="dataForm.fullCount" placeholder="Âú¼¸¼þ"></el-input>
    </el-form-item>
    <el-form-item label="´ò¼¸ÕÛ" prop="discount">
      <el-input v-model="dataForm.discount" placeholder="´ò¼¸ÕÛ"></el-input>
    </el-form-item>
    <el-form-item label="ÕÛºó¼Û" prop="price">
      <el-input v-model="dataForm.price" placeholder="ÕÛºó¼Û"></el-input>
    </el-form-item>
    <el-form-item label="ÊÇ·ñµþ¼ÓÆäËûÓÅ»Ý[0-²»¿Éµþ¼Ó£¬1-¿Éµþ¼Ó]" prop="addOther">
      <el-input v-model="dataForm.addOther" placeholder="ÊÇ·ñµþ¼ÓÆäËûÓÅ»Ý[0-²»¿Éµþ¼Ó£¬1-¿Éµþ¼Ó]"></el-input>
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
          skuId: '',
          fullCount: '',
          discount: '',
          price: '',
          addOther: ''
        },
        dataRule: {
          skuId: [
            { required: true, message: 'spu_id不能为空', trigger: 'blur' }
          ],
          fullCount: [
            { required: true, message: 'Âú¼¸¼þ不能为空', trigger: 'blur' }
          ],
          discount: [
            { required: true, message: '´ò¼¸ÕÛ不能为空', trigger: 'blur' }
          ],
          price: [
            { required: true, message: 'ÕÛºó¼Û不能为空', trigger: 'blur' }
          ],
          addOther: [
            { required: true, message: 'ÊÇ·ñµþ¼ÓÆäËûÓÅ»Ý[0-²»¿Éµþ¼Ó£¬1-¿Éµþ¼Ó]不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/coupon/skuladder/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.skuId = data.skuLadder.skuId
                this.dataForm.fullCount = data.skuLadder.fullCount
                this.dataForm.discount = data.skuLadder.discount
                this.dataForm.price = data.skuLadder.price
                this.dataForm.addOther = data.skuLadder.addOther
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
              url: this.$http.adornUrl(`/coupon/skuladder/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'skuId': this.dataForm.skuId,
                'fullCount': this.dataForm.fullCount,
                'discount': this.dataForm.discount,
                'price': this.dataForm.price,
                'addOther': this.dataForm.addOther
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
