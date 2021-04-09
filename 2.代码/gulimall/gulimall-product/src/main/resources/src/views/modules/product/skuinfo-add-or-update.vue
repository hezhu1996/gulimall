<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="spuId" prop="spuId">
      <el-input v-model="dataForm.spuId" placeholder="spuId"></el-input>
    </el-form-item>
    <el-form-item label="skuÃû³Æ" prop="skuName">
      <el-input v-model="dataForm.skuName" placeholder="skuÃû³Æ"></el-input>
    </el-form-item>
    <el-form-item label="sku½éÉÜÃèÊö" prop="skuDesc">
      <el-input v-model="dataForm.skuDesc" placeholder="sku½éÉÜÃèÊö"></el-input>
    </el-form-item>
    <el-form-item label="ËùÊô·ÖÀàid" prop="catalogId">
      <el-input v-model="dataForm.catalogId" placeholder="ËùÊô·ÖÀàid"></el-input>
    </el-form-item>
    <el-form-item label="Æ·ÅÆid" prop="brandId">
      <el-input v-model="dataForm.brandId" placeholder="Æ·ÅÆid"></el-input>
    </el-form-item>
    <el-form-item label="Ä¬ÈÏÍ¼Æ¬" prop="skuDefaultImg">
      <el-input v-model="dataForm.skuDefaultImg" placeholder="Ä¬ÈÏÍ¼Æ¬"></el-input>
    </el-form-item>
    <el-form-item label="±êÌâ" prop="skuTitle">
      <el-input v-model="dataForm.skuTitle" placeholder="±êÌâ"></el-input>
    </el-form-item>
    <el-form-item label="¸±±êÌâ" prop="skuSubtitle">
      <el-input v-model="dataForm.skuSubtitle" placeholder="¸±±êÌâ"></el-input>
    </el-form-item>
    <el-form-item label="¼Û¸ñ" prop="price">
      <el-input v-model="dataForm.price" placeholder="¼Û¸ñ"></el-input>
    </el-form-item>
    <el-form-item label="ÏúÁ¿" prop="saleCount">
      <el-input v-model="dataForm.saleCount" placeholder="ÏúÁ¿"></el-input>
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
          skuId: 0,
          spuId: '',
          skuName: '',
          skuDesc: '',
          catalogId: '',
          brandId: '',
          skuDefaultImg: '',
          skuTitle: '',
          skuSubtitle: '',
          price: '',
          saleCount: ''
        },
        dataRule: {
          spuId: [
            { required: true, message: 'spuId不能为空', trigger: 'blur' }
          ],
          skuName: [
            { required: true, message: 'skuÃû³Æ不能为空', trigger: 'blur' }
          ],
          skuDesc: [
            { required: true, message: 'sku½éÉÜÃèÊö不能为空', trigger: 'blur' }
          ],
          catalogId: [
            { required: true, message: 'ËùÊô·ÖÀàid不能为空', trigger: 'blur' }
          ],
          brandId: [
            { required: true, message: 'Æ·ÅÆid不能为空', trigger: 'blur' }
          ],
          skuDefaultImg: [
            { required: true, message: 'Ä¬ÈÏÍ¼Æ¬不能为空', trigger: 'blur' }
          ],
          skuTitle: [
            { required: true, message: '±êÌâ不能为空', trigger: 'blur' }
          ],
          skuSubtitle: [
            { required: true, message: '¸±±êÌâ不能为空', trigger: 'blur' }
          ],
          price: [
            { required: true, message: '¼Û¸ñ不能为空', trigger: 'blur' }
          ],
          saleCount: [
            { required: true, message: 'ÏúÁ¿不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.skuId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.skuId) {
            this.$http({
              url: this.$http.adornUrl(`/product/skuinfo/info/${this.dataForm.skuId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.spuId = data.skuInfo.spuId
                this.dataForm.skuName = data.skuInfo.skuName
                this.dataForm.skuDesc = data.skuInfo.skuDesc
                this.dataForm.catalogId = data.skuInfo.catalogId
                this.dataForm.brandId = data.skuInfo.brandId
                this.dataForm.skuDefaultImg = data.skuInfo.skuDefaultImg
                this.dataForm.skuTitle = data.skuInfo.skuTitle
                this.dataForm.skuSubtitle = data.skuInfo.skuSubtitle
                this.dataForm.price = data.skuInfo.price
                this.dataForm.saleCount = data.skuInfo.saleCount
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
              url: this.$http.adornUrl(`/product/skuinfo/${!this.dataForm.skuId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'skuId': this.dataForm.skuId || undefined,
                'spuId': this.dataForm.spuId,
                'skuName': this.dataForm.skuName,
                'skuDesc': this.dataForm.skuDesc,
                'catalogId': this.dataForm.catalogId,
                'brandId': this.dataForm.brandId,
                'skuDefaultImg': this.dataForm.skuDefaultImg,
                'skuTitle': this.dataForm.skuTitle,
                'skuSubtitle': this.dataForm.skuSubtitle,
                'price': this.dataForm.price,
                'saleCount': this.dataForm.saleCount
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
