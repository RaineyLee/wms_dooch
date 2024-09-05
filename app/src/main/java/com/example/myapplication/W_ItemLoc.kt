package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityItemLocBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class W_ItemLoc : AppCompatActivity(){
    private lateinit var binding_item_loc: ActivityItemLocBinding

    // 제품 번호 인식용 바코드 스캐너
    private val barcodeInfo = registerForActivityResult(
        ScanContract()
    )
    { result: ScanIntentResult ->
        // result : 스캔된 결과

        // 내용이 없다면
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else { // 내용이 있다면
        }
        binding_item_loc.textCode.setText(result.contents.toString())
    }

    // 제품 위치 인식용 바코드 스캐너
    private val locationInfo = registerForActivityResult(
        ScanContract()
    )
    { result: ScanIntentResult ->
        // result : 스캔된 결과

        // 내용이 없다면
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else { // 내용이 있다면
        }
        binding_item_loc.textItemLocInput.setText(result.contents.toString())
    }

    private fun btnitemscanClicked() {
        // 변수 초기화
        binding_item_loc.textCode.setText("")
        binding_item_loc.textItemIdKor.setText("")
        binding_item_loc.textItemIdChi.setText("")
        binding_item_loc.textItemNameKor.setText("")
        binding_item_loc.textItemQty.setText("")
        binding_item_loc.textItemLoc.setText("")
        binding_item_loc.textItemLocInput.setText("")

        // Launch ( SCAN 실행 )
        barcodeInfo.launch(ScanOptions())
    }

    private fun btnlocscanClicked() {
        // 변수 초기화
        binding_item_loc.textItemLocInput.setText("")

        // Launch ( SCAN 실행 )
        locationInfo.launch(ScanOptions())
    }
    // 콤마로 연결된 문자열을 리스트로
    fun stringToList(input: String): List<String> {
        return input.split(",").map { it.trim() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_item_loc = ActivityItemLocBinding.inflate(layoutInflater)
        setContentView(binding_item_loc.root)

        var barcode: String? = null

        val retrofit = Retrofit.Builder()
            .baseUrl("http://125.143.154.68:80/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //웹서버로 전달할 변수가 설정된 qrservice 객체 생성
        val qrservice: S_QrService = retrofit.create(S_QrService::class.java)
        val insertlocService: S_InsertLocService = retrofit.create(S_InsertLocService::class.java)

        var itemdata:D_ItemData? = null
        var itemlocinfo:D_Msg? = null

        // 제품 스캐너 실행시 이벤트
        binding_item_loc.btnScanItem.setOnClickListener {
            btnitemscanClicked()
        }

        // 위치 스캐너 실행시 이벤트
        binding_item_loc.btnScanLoc.setOnClickListener {
            btnlocscanClicked()
        }

        // 품목코드 전송시 이벤트
        binding_item_loc.btnIdSend.setOnClickListener {
            val item_id: String? = binding_item_loc.textItemIdKor.text.toString()

            qrservice.requestItemLoc(item_id).enqueue(object: Callback<D_ItemData> {
                override fun onFailure(call: Call<D_ItemData>, t: Throwable) {
//                    Log.e("LOGIN")
                    val dialog = AlertDialog.Builder(this@W_ItemLoc)
//                        Log.d("통신오류", t.message.toString())
                    dialog.setTitle("에러")
                    dialog.setMessage(t.message)
                    dialog.show()
                }

                override fun onResponse(call: Call<D_ItemData>, response: Response<D_ItemData>) {
                    itemdata = response.body()

                    if (itemdata?.item_id_kor == null) {
                        binding_item_loc.textItemIdKor.setText("")
                        binding_item_loc.textItemIdChi.setText("")
                        binding_item_loc.textItemNameKor.setText("")
                        binding_item_loc.textItemQty.setText("")
                        binding_item_loc.textItemLoc.setText("")
                    } else {
                        binding_item_loc.textItemIdKor.setText(itemdata?.item_id_kor)
                        binding_item_loc.textItemIdChi.setText(itemdata?.item_id_chi)
                        binding_item_loc.textItemNameKor.setText(itemdata?.item_name_kor)
                        binding_item_loc.textItemQty.setText(itemdata?.item_qty)
                        binding_item_loc.textItemLoc.setText(itemdata?.location)
                    }
                }
            })

        }

        // textview, edittext 문자 입력(전,후,변화)시 실행되는 이벤트 리스너. import android.text.TextWatcher
        binding_item_loc.textCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                barcode = binding_item_loc.textCode.text.toString()

                qrservice.requestQrInfo(barcode).enqueue(object: Callback<D_ItemData> {
                    override fun onFailure(call: Call<D_ItemData>, t: Throwable) {
//                    Log.e("LOGIN")
                        val dialog = AlertDialog.Builder(this@W_ItemLoc)
//                        Log.d("통신오류", t.message.toString())
                        dialog.setTitle("에러")
                        dialog.setMessage(t.message)
                        dialog.show()
                    }

                    override fun onResponse(call: Call<D_ItemData>, response: Response<D_ItemData>) {
                        itemdata = response.body()

                        if (itemdata?.item_id_kor == null) {
                            binding_item_loc.textItemIdKor.setText("")
                            binding_item_loc.textItemIdChi.setText("")
                            binding_item_loc.textItemNameKor.setText("")
                            binding_item_loc.textItemQty.setText("")
                            binding_item_loc.textItemLoc.setText("")
                        } else {
                            binding_item_loc.textItemIdKor.setText(itemdata?.item_id_kor)
                            binding_item_loc.textItemIdChi.setText(itemdata?.item_id_chi)
                            binding_item_loc.textItemNameKor.setText(itemdata?.item_name_kor)
                            binding_item_loc.textItemQty.setText(itemdata?.item_qty)
                            binding_item_loc.textItemLoc.setText(itemdata?.location)
                        }
                    }
                })
            }
        })

        // 버튼 클릭시 리스트에 저장
        binding_item_loc.btnSend.setOnClickListener {
            val id: String = binding_item_loc.textItemIdKor.text.toString()
            val location: String = binding_item_loc.textItemLoc.text.toString()
            val new_location: String = binding_item_loc.textItemLocInput.text.toString()
            var final_location: String = ""

            val result = stringToList(location)

            if (id == "") {
                val dialog = AlertDialog.Builder(this@W_ItemLoc)
                dialog.setTitle("에러")
                dialog.setMessage("입력할 위치 정보가 없습니다.")
                dialog.show()

                return@setOnClickListener

            }else if(new_location == "") {
                val dialog = AlertDialog.Builder(this@W_ItemLoc)
                dialog.setTitle("에러")
                dialog.setMessage("새로운 위치 정보가 없습니다.")
                dialog.show()

                return@setOnClickListener

            }else if(new_location in result){
                val dialog = AlertDialog.Builder(this@W_ItemLoc)
                dialog.setTitle("에러")
                dialog.setMessage("이미 입력된 위치 정보입니다.")
                dialog.show()

                binding_item_loc.textItemLocInput.setText("")
                return@setOnClickListener

            }else if(location == ""){
                final_location = new_location
            }else {
                final_location = location + "," + new_location
            }

            insertlocService.insertLocInfo(id, final_location).enqueue(object :
                Callback<D_Msg> {
                override fun onFailure(call: Call<D_Msg>, t: Throwable) {
                    val dialog = AlertDialog.Builder(this@W_ItemLoc)
                    dialog.setTitle("에러")
                    dialog.setMessage("작업 실패")
                    dialog.show()
                }
                override fun onResponse(call: Call<D_Msg>, response: Response<D_Msg>) {
                    itemlocinfo = response.body()

                    val dialog = AlertDialog.Builder(this@W_ItemLoc)
                    dialog.setTitle(itemlocinfo?.title)
                    dialog.setMessage(itemlocinfo?.message)
                    dialog.show()

                    binding_item_loc.textCode.setText(barcode)
                    binding_item_loc.textItemLocInput.setText("")
                }
            })
        }

        // 위치정보 수정 버튼 클릭시
        binding_item_loc.btnLocEdit.setOnClickListener {
            val id: String = binding_item_loc.textItemIdKor.text.toString()
            val final_location: String = binding_item_loc.textItemLoc.text.toString()

            insertlocService.insertLocInfo(id, final_location).enqueue(object :
                Callback<D_Msg> {
                override fun onFailure(call: Call<D_Msg>, t: Throwable) {
                    val dialog = AlertDialog.Builder(this@W_ItemLoc)
                    dialog.setTitle("에러")
                    dialog.setMessage(t.message)
                    dialog.show()
                }
                override fun onResponse(call: Call<D_Msg>, response: Response<D_Msg>) {
                    itemlocinfo = response.body()

                    val dialog = AlertDialog.Builder(this@W_ItemLoc)
                    dialog.setTitle(itemlocinfo?.title)
                    dialog.setMessage(itemlocinfo?.message)
                    dialog.show()

                    binding_item_loc.textCode.setText(barcode)
                    binding_item_loc.textItemLocInput.setText("")
                }
            })
        }

        binding_item_loc.btnMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}