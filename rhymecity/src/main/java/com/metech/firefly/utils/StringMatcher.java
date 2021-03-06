/*
 * Copyright 2011 woozzu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.metech.firefly.utils;

public class StringMatcher
{
	public static boolean match(String value, String keyword)
	{
		if (value == null || keyword == null)
		{
			return false;
		}
		if (keyword.length() > value.length())
		{
			return false;
		}

		int i = 0, j = 0;
		do
		{
			int vi = value.charAt(i);
			int kj = keyword.charAt(j);
			if (isKorean(vi) && isInitialSound(kj))
			{
			}
			else
			{
				if (vi == kj)
				{
					i++;
					j++;
				}
				else if (j > 0)
				{
					break;
				}
				else
				{
					i++;
				}
			}
		}
		while (i < value.length() && j < keyword.length());

		return (j == keyword.length()) ? true : false;
	}

	private static boolean isKorean(int i)
	{
		return false;
	}

	private static boolean isInitialSound(int i)
	{
		return false;
	}
}