package sample04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class CodeSearch extends SearchSys {
	@Override
	protected void init() {
		if(Pattern.matches("\\d{1,7}", AddressSearch.input1))
			AddressSearch.isInputOK = true;
		else
			AddressSearch.isInputOK = false;
	}
	
	@Override
	protected void search() {
		var line = "読込結果";

		try(var reader = Files.newBufferedReader(Paths.get(AddressSearch.fileName))){
			while((line = reader.readLine()) != null) {
				// カンマで分割
				String[] items = line.split(",");
				if(items.length == 9) {
					// Data型で保存・・・郵便番号(7桁）,カナ住所,住所1（都道府県）,住所2（市区町村以下）
					var item = setData(items);
					// 郵便番号前方一致で検索
					if(item.HitCode(AddressSearch.input1)) {
						//リストに追加
						AddressSearch.searchResults.add(item);
					}
				}
				else {
					AddressSearch.isInputOK = false;
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void sort() {
		Data.CodeSort(AddressSearch.searchResults);
	}
}
